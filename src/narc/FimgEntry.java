package narc;

import java.io.*;

public class FimgEntry {
	private ByteArrayOutputStream entryData;
	
	public FimgEntry()
	{
		entryData=new ByteArrayOutputStream();
	}

	public ByteArrayOutputStream getEntrydata() {
		return entryData;
	}

	public void setEntrydata(ByteArrayOutputStream entrydata) {
		this.entryData = entrydata;
	}
	
	public byte[] getEntryData()
	{
		return this.entryData.toByteArray();
	}
	
	public void setEntryData(byte[] data) throws IOException
	{
		entryData.reset();
		entryData.write(data);
	}
}
