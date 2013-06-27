package net.mydebug.chessgames.drive;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;

public class Serialize {
	public static byte[] serialize(Object obj)  {
	    ByteArrayOutputStream bos = new ByteArrayOutputStream();
	    ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(bos);
		    oos.writeObject(obj);
		    oos.flush();
		    oos.close();
		    bos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    byte[] data = bos.toByteArray();
	    return data;
	}


	
	public static Object deserialize( byte[] serialized ) {
		ByteArrayInputStream bis = new ByteArrayInputStream(serialized);
		try {
			ObjectInputStream ois = new ObjectInputStream(bis);
			Object obj = (Object) ois.readObject();
			return obj;
		} catch (OptionalDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
}
