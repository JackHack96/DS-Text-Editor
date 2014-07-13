package narc;

import java.nio.*;
import java.nio.channels.FileChannel;
import java.io.*;

public class HexInputStream {

	private FileChannel dis;
	
	public HexInputStream(FileInputStream f)
	{
		dis=f.getChannel();
	}
	
	public int readByte() throws IOException
	{
		ByteBuffer buffer=ByteBuffer.allocate(1).order(ByteOrder.LITTLE_ENDIAN);
		dis.read(buffer);
		buffer.flip();
		return buffer.getInt();
	}
	
	public int readShort() throws IOException
	{
		ByteBuffer buffer=ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);
		dis.read(buffer);
		buffer.flip();
		return buffer.getInt();
	}
	
	public int readInt() throws IOException
	{
		ByteBuffer buffer=ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
		dis.read(buffer);
		buffer.flip();
		return buffer.getInt();
	}
	
	public long readLong() throws IOException
	{
		ByteBuffer buffer=ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
		dis.read(buffer);
		buffer.flip();
		return buffer.getLong();
	}
	
	public byte[] readBuffer(int len) throws IOException
	{
		ByteBuffer buffer=ByteBuffer.allocate(len).order(ByteOrder.LITTLE_ENDIAN);
		dis.read(buffer);
		buffer.flip();
		return buffer.array();
	}
	
	public void seek(long l) throws IOException
	{
		dis.position(l);
	}
	
	public void skip(int t) throws IOException
	{
		dis.position(dis.position()+t);
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