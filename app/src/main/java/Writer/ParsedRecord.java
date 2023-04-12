package writer;


public interface ParsedRecord {

	public static final int TYPE_TEXT = 1;
	public static final int TYPE_URI = 2;
	public static final int TYPE_PRODUCT = 8;
	public static final int TYPE_BITMAP = 3;

	public int getType();

}
