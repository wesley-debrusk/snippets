# CSE 3300 Programming Assignment 3 Exercise 0
# Client side program
# Wesley DeBrusk
# wrd13001

# Import the socket and random library
import socket
import random

# Set up the IP addresses and ports for the client and server
server_ip = socket.gethostbyname('tao.ite.uconn.edu')
server_port = 3300
client_ip = socket.gethostbyname(socket.gethostname())
client_port = 5500

# Create the socket and connect using the server IP and port
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect((server_ip, server_port))

# Construct and print the request message
ws = ' '
request_type = 'ex0'
server_specifier = str(server_ip) + '-' + str(server_port)
client_specifier = str(client_ip) + '-' + str(client_port)
conn_specifier = server_specifier + ws + client_specifier
usernum_int = random.randint(1000, 8000)
usernum = str(usernum_int)
username = 'W.R.DeBrusk'
newline = '\n'
request_str = request_type + ws + conn_specifier + ws + usernum + ws + username + newline
print("Sent to CSE 3300 server:")
print(request_str)

# Send the request message and recieve the response from server
s.send(request_str.encode())
data = s.recv(4096).decode()
# Print the response from the server
print("Recieved from CSE 3300 server:")
print(data)

# Check if the server responded with an "OK" message
check_ok = 'OK' in data
if check_ok:
    # If the server response is OK, construct and print the ACK string
    add_2 = int(usernum) + 2
    new_usernum = str(add_2)
    data_split = data.split()
    servernum = data_split[-1];
    servernum_int = int(servernum)
    servernum_int += 1
    servernum = str(servernum_int)
    ack_str = request_type + ws + new_usernum + ws + servernum + newline
    print("Sent to CSE 3300 server:")
    print(ack_str)

    # Send ACK string and recieve response from server, print the response
    s.send(ack_str.encode())
    data = s.recv(4096).decode()
    print("Recieved from CSE 3300 server:")
    print(data)
else:
    # If the server doesn't respond with "OK", print error message
    print('Request message failed.')

# Close the connection
s.close()
