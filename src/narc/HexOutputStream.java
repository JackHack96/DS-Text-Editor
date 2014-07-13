package narc;

import java.nio.*;
import java.nio.channels.FileChannel;
import java.io.*;

public class HexOutputStream {

	private FileChannel dis;
	
	public HexOutputStream(FileOutputStream f)
	{
		dis=f.getChannel();
	}
	
	public void writeByte(byte t) throws IOException
	{
		ByteBuffer buffer=ByteBuffer.allocate(1).order(ByteOrder.LITTLE_ENDIAN);
		buffer.put(t);
		buffer.flip();
		dis.write(buffer);
	}
	
	public void writeShort(short t) throws IOException
	{
		ByteBuffer buffer=ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);
		buffer.putShort(t);
		buffer.flip();
		dis.write(buffer);
	}
	
	public void writeInt(int t) throws IOException
	{
		ByteBuffer buffer=ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
		buffer.putInt(t);
		buffer.flip();
		dis.write(buffer);
	}
	
	public void writeLong(long t) throws IOException
	{
		ByteBuffer buffer=ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
		buffer.putLong(t);
		buffer.flip();
		dis.write(buffer);
	}
	
	public void writeBuffer(ByteArrayOutputStream t) throws IOException
	{
		ByteBuffer buffer=ByteBuffer.wrap(t.toByteArray());
		dis.write(buffer);
	}
	
	public void seek(long t) throws IOException
	{
		dis.position(t);
	}
	
	public int getPosition() throws IOException
	{
		return (int)dis.position();
	}
	
	public void close() throws IOException
	{
		dis.close();
	}
}