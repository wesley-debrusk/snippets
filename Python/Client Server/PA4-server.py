# CSE 3300 Programming Assignment 4 Exercise 1
# Server side program
# Wesley DeBrusk
# wrd13001

# Imports
import socket
import struct
import random
import ipaddress
from urllib.request import urlopen

# Mask used for ones complement addition
mask = 0x0000ffff

# Used to calculate one's complement sum
def sum(unsigned16_1, unsigned16_2):
    result = unsigned16_1 + unsigned16_2
    if result > mask:
        result = (result & mask) + 1
    return result;

# Used to generate the checksum using the sum function
def calculate_checksum(header, labinfo, cookie, ip, port):
    sofar = sum(header, labinfo)
    cookie_shift = cookie >> 16
    sofar = sum(sofar, cookie_shift)
    cookie_mask = cookie & mask
    sofar = sum(sofar, cookie_mask)
    ip_shift = ip >> 16
    sofar = sum(sofar, ip_shift)
    ip_mask = ip & mask
    sofar = sum(sofar, ip_mask)
    sofar = sum(sofar, port)
    result = ~sofar & mask
    return result;

# Get PO box from database
def getPO(db, ssn):
    for x in range(0, 102, 2):
        if int(db[x].decode('UTF-8')) == ssn:
            return int(db[x+1].decode('UTF-8'));

# Set up the IP addresses and ports for the client and server
cse_ip = socket.gethostbyname('tao.ite.uconn.edu')
cse_port = 3301
ip = socket.gethostbyname(socket.gethostname())
port = 5500
server_address = (ip, port)
cse_address = (cse_ip, cse_port)

# Load database
db = urlopen('http://engr.uconn.edu/~song/classes/cn/db').read()
db = db.split()

# Get the probe message ready
header = 36068
lab_info = 1031
cookie = random.randint(0, 100000)
ip_int = int(ipaddress.IPv4Address(ip))
checksum = calculate_checksum(header, lab_info, cookie, ip_int, port)
message = struct.pack('!2H2I2H', header, lab_info, cookie, ip_int, checksum, port)
sendsock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

# Send probe message
sendsock.sendto(message, cse_address)
print("Sent probe to:\t\t", cse_address)

# Set up socket to listen on
listensock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
listensock.bind(server_address)
listensock.settimeout(5.0)
print("Started server on:\t", server_address)

# Listen for incoming messages
while True:
    print("-------------Ready to recieve message-------------")

    # Recieve message
    data, address = listensock.recvfrom(4096)
    message = struct.unpack('!2H2I2H', data)
    print("Recieved:\t", message)

    # Verify checksum
    recieved_checksum = calculate_checksum(message[0], message[1], message[2], message[3], message[5])
    if recieved_checksum == message[4]:
        print("Checksum OK")
    else:
        print("Checksum error")

    # Verify header
    if message[0] == 3300:
        print("Header OK:\t", message[0])
        header = message[0]
    else:
        print("Header error:\t", message[0])

    # Verify lab info
    if message[1] == 1031:
        print("Lab info OK:\t", message[1])
        lab_info = message[1]
    else:
        print("Lab info error:\t", message[1])
        break

    # Get response message ready
    header = 19684
    cookie = message[2]
    ssn = message[3]
    box = getPO(db, ssn)

    # Send response
    if box is not None:
        print("PO box:\t", box)
        checksum = calculate_checksum(header, lab_info, cookie, ssn, box)
        newmessage = struct.pack('!2H2I2H', header, lab_info, cookie, ssn, checksum, box)
        sendsock.sendto(newmessage, cse_address)
    else:
        print("Invalid SSN")
        checksum = calculate_checksum(header, lab_info, cookie, ssn, 32771)
        newmessage = struct.pack('!2H2I2H', header, lab_info, cookie, ssn, checksum, 32771)
        sendsock.sendto(newmessage, cse_address)
