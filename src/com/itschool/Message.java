package com.itschool;

import java.io.*;
import java.util.Date;

public class Message implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public Date date = new Date();
	public String from;
	public String to;
	
	public boolean isFile;
	public transient String text;
	public transient String path;

	public static Message readFromStream(InputStream in)
			throws IOException, ClassNotFoundException
	{
		if (in.available() <= 0)
		{
			return null;
		}

		DataInputStream ds = new DataInputStream(in);
		int len = ds.readInt();
		byte[] packet = new byte[len];
		ds.read(packet);

		ByteArrayInputStream bs = new ByteArrayInputStream(packet);
		ObjectInputStream os = new ObjectInputStream(bs);
		try
		{
			Message msg = (Message) os.readObject();
			if (!msg.isFile)
			{
				msg.text = (String) os.readUTF();
			}
			else
			{
				// чтение содержимого файла
			}

			return msg;
		}
		finally
		{
			os.close();
		}
	}
	
	@Override
	public String toString() {
		return new StringBuilder()
			.append("[")
			.append(date.toString())
				.append(", От: ")
			.append(from)
				.append(", для: ")
			.append(to)
			.append("] ")
			.append(text)
			.toString();
	}
	
	public void writeToStream(OutputStream out)
		throws IOException
	{
		ByteArrayOutputStream bs = new ByteArrayOutputStream();
		ObjectOutputStream os = new ObjectOutputStream(bs);
		try {
			os.writeObject(this);

			if ( ! isFile) {
				os.writeUTF(text);
			} else {
				// запись содержимого файла
			}
		} finally {
			os.flush();
			os.close();
		}

		byte[] packet = bs.toByteArray();

		DataOutputStream ds = new DataOutputStream(out);
		ds.writeInt(packet.length);
		ds.write(packet);
		ds.flush();
	}
}