package com.netease.yuedu.weekly.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class KryoUtil {

	private static Kryo kryo = new Kryo();
	
	public static byte[] obj2Bytes(Object obj) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Output output = new Output(baos);
		kryo.writeObject(output, obj);
		output.close();
		byte[] bytes = baos.toByteArray();
		try {
			baos.close();
		} catch (IOException e) {
			// do nothing, because ByteArrayOutputStream.close() does nothing at all
		}
		return bytes;
	}
	
	public static <T> T bytes2Obj(byte[] bytes, Class<T> clazz) {
		Input input = new Input(bytes);
		T result = kryo.readObject(input, clazz);
		input.close();
		return result;
	}
	
}
