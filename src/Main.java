import java.util.concurrent.*;

public class Main
{
	public static void main2(String[] args)
	{

		new Main().a();

	}
	public void a()
	{
		for (long l=0;l < 1000000;l += 600)
		{
			String hms=timeMillisToMin(l + "");
			String mls=timeMinToMillis(hms);
			System.out.println(((l + "").equals(mls) ?"✓ ": "  ") + hms + "  ,  " + mls);
		}
	}
	public String timeMillisToMin(String tm)
	{
		try
		{
			long millis=Long.parseLong(tm);
			return String.format("%02d:%02d.%02d", TimeUnit.MILLISECONDS.toMinutes(millis),
								 TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1),
								 TimeUnit.MILLISECONDS.toMillis(millis) % TimeUnit.SECONDS.toMillis(1));
		}
		catch (Exception e)
		{}
		return "";
	}
	public String timeMinToMillis(String tm)
	{
		try
		{
			String mil=tm.substring(tm.lastIndexOf(".") + 1);
			String mms=tm.substring(0, tm.indexOf("."));
			String min=mms.substring(0, mms.indexOf(":"));
			String sec=mms.substring(mms.indexOf(":") + 1);
			long minT=Long.parseLong(min);
			long secT=Long.parseLong(sec);
			long milT=Long.parseLong(mil);
			long total=TimeUnit.MINUTES.toMillis(minT);
			total += TimeUnit.SECONDS.toMillis(secT);
			total += TimeUnit.MILLISECONDS.toMillis(milT);
			return "" + total;
		}
		catch (Exception e)
		{}
		return "";
	}
}
