package state;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

@Deprecated
public class StateStorage {

	public static void store(Object ... objects) throws IOException {
		final FileOutputStream fos = new FileOutputStream(String.format("%d.data", System.currentTimeMillis()));
		final ObjectOutputStream oos = new ObjectOutputStream(fos);
		for (final Object o : objects) {
			oos.writeObject(o);			
		}
		oos.flush();
		oos.close();
	}

	public static Object restore(String id) throws Exception {
		final FileInputStream fis = new FileInputStream(id + ".data");
		final ObjectInputStream ois = new ObjectInputStream(fis);	
		final Object result = ois.readObject();
		ois.close();
		return result;
	}
	
}
