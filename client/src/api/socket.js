import socketIOClient from "socket.io-client";
import { toast } from 'react-toastify';

export const getSocketClient = () => {
    if (!localStorage.getItem("socket_ip")) {
        localStorage.setItem("socket_ip", "localhost");
    }
    if (!localStorage.getItem("socket_port")) {
        localStorage.setItem("socket_port", "8000");
    }

    const socketIp = localStorage.getItem("socket_ip");
    const socketPort = localStorage.getItem("socket_port");

    let socketClient = socketIOClient(`http://${socketIp}:${socketPort}`);

    // add toast event handler here
    socketClient.on("toast_info", msg => {
        toast.info(msg);
    });

    return socketClient;
}

export let socketClient = getSocketClient();
