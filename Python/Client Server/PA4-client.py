# CSE 3300 Programming Assignment 4 Exercise 0
# Client side program
# Wesley DeBrusk
# wrd13001

# Imports
import socket
import struct
import random

# Used to check the transaction outcome
def checkbit(str):
    s = list(str)
    if s[0] == "0":
        return True
    elif s[0] == "1":
        return False;

# Mask used for ones complement addition
mask = 0x0000ffff

# Used to calculate one's complement sum
def sum(unsigned16_1, unsigned16_2):
    result = unsigned16_1 + unsigned16_2
    if result > mask:
        result = (result & mask) + 1
    return result;

# Used to generate the checksum using the sum function
def calculate_checksum(header, labinfo, cookie, ssn, last):
    sofar = sum(header, labinfo)
    cookie_shift = cookie >> 16
    sofar = sum(sofar, cookie_shift)
    cookie_mask = cookie & mask
    sofar = sum(sofar, cookie_mask)
    ssn_shift = ssn >> 16
    sofar = sum(sofar, ssn_shift)
    ssn_mask = ssn & mask
    sofar = sum(sofar, ssn_mask)
    sofar = sum(sofar, last)
    result = ~sofar & mask
    return result;

# Set up the IP addresses and ports for the client and server
server_ip = socket.gethostbyname('tao.ite.uconn.edu')
server_port = 3301

# Create socket and set timeout
udpsocket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
udpsocket.settimeout(5.0)
server_address = (server_ip, server_port)
print('Connected to: ', server_address)

# Fill in fields for the message to send
header = 3300
lab_info = 1031
cookie = random.randint(0, 100000)
social = int(input("Enter a social security number: "))
checksum = 0
last = 0

# Calculate the checksum using previously defined function
checksum = calculate_checksum(header, lab_info, cookie, social, last)

# Pack the message in a struct and send it
message = struct.pack('!2H2I2H', header, lab_info, cookie, social, checksum, last)
print("Sent:\t\t", struct.unpack('!2H2I2H', message))

# Send up to five times if necessary
counter = 1
flag = 0
while counter < 6:
    try:
        udpsocket.sendto(message, server_address)
        data, server = udpsocket.recvfrom(4096)
        if data:
            flag = 1
            break
        else:
            continue
    except socket.timeout:
        print("Timeout:", counter)
    counter += 1

# If the server responds
if flag == 1:
    # Unpack and print the recieved message
    recieved = struct.unpack('!2H2I2H', data)
    print("Recieved:\t", recieved)

    # Parse through the recieved message
    recieved_header = recieved[0]
    recieved_lab_info = recieved[1]
    recieved_cookie = recieved[2]
    recieved_ssn = recieved[3]
    recieved_checksum = recieved[4]
    recieved_outcome = recieved[5]

    # Calculate the checksum of the recieved message
    new_checksum = calculate_checksum(recieved_header, recieved_lab_info, recieved_cookie, recieved_ssn, recieved_outcome)

    # Verify that the checksum is correct
    if new_checksum == recieved_checksum:
        print("Checksum OK:\t", recieved_checksum)
    else:
        print("Checksum error")

    # Verift the header is correct
    if recieved_header == 19684:
        print("Header OK:\t", recieved_header)
    else:
        print("Header error:\t", recieved_header)

    # Verify the lab number and version are correct
    if recieved_lab_info == 1031:
        print("Lab info OK:\t", recieved_lab_info)
    else:
        print("Lab info error:\t", recieved_lab_info)

    # Verify the cookie is correct
    if recieved_cookie == cookie:
        print("Cookie OK:\t", recieved_cookie)
    else:
        print("Cookie error:\t", recieved_cookie)

    # Verify the SSN is correct
    if recieved_ssn == social:
        print("Social OK:\t", recieved_ssn)
    else:
        print("SSN error:\t", recieved_ssn)

    # Check the transaction outcome
    outcome_string = '{0:016b}'.format(recieved_outcome)

    # If OK, print the P.O. Box
    if checkbit(outcome_string) == True:
        print("P.O. box:\t", recieved_outcome)
    # If not OK print the error
    else:
        error_code = int(outcome_string[4:], 2)
        if error_code == 1:
            print("Checksum error")
        elif error_code == 2:
            print("Syntax error")
        elif error_code == 3:
            print("Error: Unknown SSN")
        elif error_code == 4:
            print("Server error")
