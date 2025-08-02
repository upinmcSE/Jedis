import socket

def read_response(sock):
    buffer = b""
    while True:
        part = sock.recv(1024)
        if not part:
            break
        buffer += part
        # Thoát nếu đã đọc đủ một RESP message
        if b"\r\n" in buffer:
            break
    return buffer.decode()

def send_and_receive(cmd):
    with socket.create_connection(("localhost", 6379)) as s:
        s.sendall(cmd)
        response = read_response(s)
        print("Response:", repr(response))

# RESP PING
# send_and_receive(b"*1\r\n$4\r\nPING\r\n")

# # RESP ECHO "Hello"
# send_and_receive(b"*2\r\n$4\r\nECHO\r\n$5\r\nHello\r\n")

# RESP SET mykey myvalue
send_and_receive(b"*3\r\n$3\r\nSET\r\n$5\r\nmykey\r\n$7\r\nmyvalue\r\n")

# # RESP GET mykey
# send_and_receive(b"*2\r\n$3\r\nGET\r\n$5\r\nmykey\r\n")
