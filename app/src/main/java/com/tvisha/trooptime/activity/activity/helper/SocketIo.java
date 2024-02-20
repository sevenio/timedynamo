package com.tvisha.trooptime.activity.activity.helper;

import android.content.Context;
import android.content.SharedPreferences;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by akhil on 28/5/18.
 */

public class SocketIo {


    private static final SocketIo ourInstance = new SocketIo();
    public static android.os.Handler handler;
    Socket socket;
    private Context context;
    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            handler.obtainMessage(SocketConstants.SOCKET_CONNECT).sendToTarget();
        }
    };
    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            handler.obtainMessage(SocketConstants.SOCKET_DISCONNECT).sendToTarget();
        }
    };
    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

        }
    };
    private Emitter.Listener syncUser = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                handler.obtainMessage(SocketConstants.SOCKET_SYNC_USER, args[0]).sendToTarget();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    private Emitter.Listener syncData = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                handler.obtainMessage(SocketConstants.SOCKET_SYNC_DATA, args[0]).sendToTarget();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    private Emitter.Listener syncAttendance = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                handler.obtainMessage(SocketConstants.SOCKET_SYNC_ATTENDANCE, args[0]).sendToTarget();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    private Emitter.Listener syncBreak = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                handler.obtainMessage(SocketConstants.SOCKET_SYNC_BREAK, args[0]).sendToTarget();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private SocketIo() {
    }

    public static SocketIo getInstance() {
        return ourInstance;
    }

    public Socket getSocket() {
        return socket;
    }

    public void connectSocket() {
        try {
            if (socket == null) {
                // SharedPreferences sharedPreferences = getInstance().context.getSharedPreferences("userrecord",0);
                SharedPreferences sharedPreferences = getInstance().context.getSharedPreferences(SharePreferenceKeys.SP_NAME, MODE_PRIVATE);
                IO.Options opts = new IO.Options();

                // String token="ExN60jFdtSH1IUABa80ODkDXsUg1J3Bo";
                String token = sharedPreferences.getString(SharePreferenceKeys.API_KEY, "");
                opts.forceNew = true;
                opts.query = "token=" + token;
                opts.reconnection = true;
                socket = IO.socket(ServerUrls.SOCKET_URL, opts);
            }

            if (!socket.connected()) {
                socket.connect();
                initSocketListener();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disconnectSocket() {
        if (socket != null) {
            socket.disconnect();
            socket = null;
        }
    }

    public Boolean isSocketConnected() {
        Boolean status = false;
        if (socket != null) {
            status = socket.connected();
        }
        return status;
    }

    public void initSocketListener() {
        //removeSocketListener();
        socket.on(Socket.EVENT_CONNECT, onConnect);
        socket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        socket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        socket.on(SocketConstants.EVENT_SYNC_USER, syncUser);
        socket.on(SocketConstants.EVENT_SYNC_DATA, syncData);
        socket.on(SocketConstants.EVENT_SYNC_ATTENDANCE, syncAttendance);
        socket.on(SocketConstants.EVENT_SYNC_BREAK, syncBreak);
    }

    public void removeSocketListener() {
        socket.off(Socket.EVENT_CONNECT, onConnect);
        socket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        socket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        socket.off(SocketConstants.EVENT_SYNC_USER, syncUser);
        socket.off(SocketConstants.EVENT_SYNC_DATA, syncData);
        socket.off(SocketConstants.EVENT_SYNC_ATTENDANCE, syncAttendance);
        socket.off(SocketConstants.EVENT_SYNC_BREAK, syncBreak);
    }

    public void setHandlers(android.os.Handler socketHandler) {
        handler = socketHandler;
    }

    public void setContext(Context applicationContext) {
        context = applicationContext;
    }
}
