package narc;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;

public class Narc {
	private long narcSize, numEntries, fatbSize, fntbSize, fimgSize, fimgOffset;
	private Path narcPath;
	private ArrayList<FatbEntry> fatbEntry;
	private ArrayList<FimgEntry> fimgEntry;

	public ArrayList<FimgEntry> getFimgEntry() {
		return fimgEntry;
	}

	public void setFimgEntry(ArrayList<FimgEntry> fimgEntry) {
		this.fimgEntry = fimgEntry;
	}

	public ArrayList<FatbEntry> getFatbEntry() {
		return fatbEntry;
	}

	public void setFatbEntry(ArrayList<FatbEntry> fatbEntry) {
		this.fatbEntry = fatbEntry;
	}

	public Narc()
	{

	}

	public Narc(long narcSize, long fatbSize, long numEntries, long fntbSize)
	{
		this.narcSize=narcSize;
		this.fatbSize=fatbSize;
		this.numEntries=numEntries;
		this.fntbSize=fntbSize;
	}

	public Narc(String narcPath) throws IOException
	{
		FatbEntry temp;
		FimgEntry temp2;
		fatbEntry=new ArrayList<FatbEntry>();
		fimgEntry=new ArrayList<FimgEntry>();
		this.narcPath=Paths.get(narcPath);
		try {
			HexInputStream prova=new HexInputStream(new FileInputStream(narcPath));
			prova.skip(8);
			narcSize=prova.readInt();
			prova.skip(8);

			//This is FATB stuff
			fatbSize=prova.readInt();
			numEntries=prova.readInt();
			for(int i=0;i<numEntries;i++)
			{
				temp=new FatbEntry();
				temp.setStartOffset(prova.readInt());
				temp.setEndOffset(prova.readInt());
				temp.setEntrySize(temp.getEndOffset()-temp.getStartOffset());
				fatbEntry.add(temp);
			}

			//This is FNTB stuff
			prova.skip(4);
			fntbSize=prova.readInt();
			prova.skip((int) (fntbSize));

			//This is FIMG stuff
			fimgOffset=prova.getPosition();
			prova.skip(4);
			fimgSize=prova.readInt();
			for(int i=0;i<numEntries;i++)
			{
				temp2=new FimgEntry();
				prova.seek(fimgOffset+fatbEntry.get(i).getStartOffset());
				temp2.setEntryData(prova.readBuffer(fatbEntry.get(i).getEntrySize()));
				fimgEntry.add(temp2);
			}
			prova.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public long getFimgSize() {
		return fimgSize;
	}

	public void setFimgSize(long fimgSize) {
		this.fimgSize = fimgSize;
	}

	public Path getNarcPath() {
		return narcPath;
	}

	public void setNarcPath(Path narcPath) {
		this.narcPath = narcPath;
	}

	public long getNarcSize() {
		return narcSize;
	}

	public void setNarcSize(long narcSize) {
		this.narcSize = narcSize;
	}

	public long getFatbSize() {
		return fatbSize;
	}

	public void setFatbSize(long fatbSize) {
		this.fatbSize = fatbSize;
	}

	public long getnumEntries() {
		return numEntries;
	}

	public void setnumEntries(long numEntries) {
		this.numEntries = numEntries;
	}

	public long getFntbSize() {
		return fntbSize;
	}

	public void setFntbSize(long fntbSize) {
		this.fntbSize = fntbSize;
	}

	public void writeNarc(String narcPath) throws IOException
	{
		HexOutputStream output = new HexOutputStream(new FileOutputStream(narcPath));
		output.writeInt(1129464142);
		output.writeInt(16842750);
		int offset=output.getPosition();
		output.writeInt(0);
		output.writeShort((short)16);
		output.writeShort((short)3);
		output.writeInt(1178686530);
		output.writeInt(12+fimgEntry.size()*8);
		output.writeInt(fimgEntry.size());
		int num=0;
		for(int i=0;i<fimgEntry.size();i++)
		{
			while(num%4!=0)
				num++;
			output.writeInt(num);
			num+=fimgEntry.get(i).getEntrydata().size();
			output.writeInt(num);
		}
		output.writeInt(1179538498);
		output.writeInt(16);
		output.writeInt(4);
		output.writeInt(65536);
		output.writeInt(1179209031);
		int offset2=output.getPosition();
		output.writeInt(0);
		num=0;
		for(int i=0;i<fimgEntry.size();i++)
		{
			while(num%4!=0)
			{
				output.writeByte((byte) 255);
				num++;
			}
			output.writeBuffer(fimgEntry.get(i).getEntrydata());
			num+=fimgEntry.get(i).getEntrydata().size();
		}
		int value=output.getPosition();
		output.seek(offset);
		output.writeInt(value);
		output.seek(offset2);
		output.writeInt(num+8);
		output.close();
	}
} 
