# CSE 3300 Programming Assignment 3 Exercise 1
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

# Create the first socket and connect using the server IP and port
psock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
psock.connect((server_ip, server_port))

# Construct and print the request message
ws = ' '
request_type = 'ex1'
server_specifier = str(server_ip) + '-' + str(server_port)
client_specifier = str(client_ip) + '-' + str(client_port)
conn_specifier = server_specifier + ws + client_specifier
usernum_int = random.randint(1000, 8000)
usernum = str(usernum_int)
username = 'W.R.DeBrusk'
newline = '\n'
request_str = request_type + ws + conn_specifier + ws + usernum + ws + username + newline
print("Sent to CSE 3300 server on connection 1:")
print(request_str)

# Create second socket, bind it using the client IP and port, and listen
newsock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
newsock.bind((client_ip, client_port))
newsock.listen()

# Recieve OK message on first socket
psock.send(request_str.encode())
data = psock.recv(4096).decode()
print("Recieved from CSE 3300 server on connection 1:")
print(data)

# Make sure "OK" is in the response message
check_ok = 'OK' in data
if check_ok:
    # If OK, add one to the servernum
    data_split = data.split()
    servernum = data_split[-1];
    servernum_int = int(servernum)
    servernum_int += 1
    servernum = str(servernum_int)

    # Recieve message on second socket, and print
    conn, addr = newsock.accept()
    print('Connected to by:', addr)
    data = conn.recv(4096).decode()
    print("Recieved from CSE 3300 server on connection 2:")
    print(data)

    # Add one to the new servernum
    data_split = data.split()
    newservernum = data_split[-1];
    newservernum_int = int(newservernum)
    newservernum_int += 1
    newservernum = str(newservernum_int)

    # Print and send reply message over second socket
    reply = servernum + ws + newservernum + newline
    print("Sent to CSE 3300 server on connection 2:")
    print(reply)
    conn.send(reply.encode())
    # Close second socket
    newsock.close()

    # Recieve and print response from first socket
    data = psock.recv(4096).decode()
    print("Recieved from CSE 3300 server on connection 1:")
    print(data)
    # Close first socket
    psock.close()

else:
    # Print that the response message did not contain "OK"
    print("Request message failed.")
    newsock.close()
    psock.close()
