package com.creative.share.apps.aqaar.models;

import java.io.Serializable;

public class MessageModel  implements Serializable {
    private SingleMessageModel data;

    public SingleMessageModel getData() {
        return data;
    }

    public class SingleMessageModel implements Serializable {
        private int id;
                private String send_message_id;
                private String send_message_name;
        private String receive_message_id;
        private String message;
        private String read_message;
                private String date;

        public int getId() {
            return id;
        }

        public String getSend_message_id() {
            return send_message_id;
        }

        public String getSend_message_name() {
            return send_message_name;
        }

        public String getReceive_message_id() {
            return receive_message_id;
        }

        public String getMessage() {
            return message;
        }

        public String getRead_message() {
            return read_message;
        }

        public String getDate() {
            return date;
        }
    }
}