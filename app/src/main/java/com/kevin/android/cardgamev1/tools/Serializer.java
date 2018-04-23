package com.kevin.android.cardgamev1.tools;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;


public class Serializer {
   static String TAG = "! ! ! ! ! LOG !";
        public static byte[] serialize(Object obj, byte[] code) throws IOException {
            Log.v(TAG,"SERIALIZE CALLED");
            try(ByteArrayOutputStream b = new ByteArrayOutputStream()){
                try(ObjectOutputStream o = new ObjectOutputStream(b)){
                    o.writeObject(obj);
                }
                int size = b.toByteArray().length;
             // send confirmation code?   long confirm = Math.round(1000000);
                byte[] bytes2 = new byte[size + 1];
                bytes2[0] = code[0];

                byte[] bytes = b.toByteArray();

                for (int i = 0; i < size; i++){
                bytes2[i + 1] = bytes[i];
                }
                return bytes2;
            }
        }

        public static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
            Log.v(TAG,"DESERIALIZE CALLED");
            byte[] bytes1 = new byte[bytes.length - 1];
            for (int i = 1; i < bytes.length; i++){
                bytes1[i - 1] = bytes[i];
            }
            try(ByteArrayInputStream b = new ByteArrayInputStream(bytes)){
                try(ObjectInputStream o = new ObjectInputStream(b)){
                    return o.readObject();
                }
            }
        }

    public static byte[] convertToBytes(Object object) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutput out = new ObjectOutputStream(bos)) {
            out.writeObject(object);
            return bos.toByteArray();
        }
    }
    public static Object convertFromBytes(byte[] bytes) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
             ObjectInput in = new ObjectInputStream(bis)) {
            return in.readObject();
        }
    }

}
