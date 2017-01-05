package getTicket;
import java.io.*;
import java.net.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.regex.*;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.swing.JOptionPane;

import java.util.ArrayList;
public class Test {

    public static void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[] {};
                    }
 
                    public void checkClientTrusted(X509Certificate[] chain, String authType)
                            throws CertificateException {
                    }
 
                    public void checkServerTrusted(X509Certificate[] chain, String authType)
                            throws CertificateException {
                    }
                }
        };
     
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"失败！遇到严重错误，请重启！");
        }
    }
 
	public static String SendGet(String url) throws IOException {
			trustAllHosts();
		  // 定义一个字符串用来存储网页内容
		  String result = "";
		  // 定义一个缓冲字符输入流
		  BufferedReader in = null;
		   // 将string转成url对象
		   URL realUrl = new URL(url);
		   // 初始化一个链接到那个url的连接
		  // URLConnection connection =realUrl.openConnection();
		   HttpURLConnection connection =(HttpURLConnection) realUrl.openConnection();
		   // 开始实际的连接
		  // connection.setRequestProperty("Accept-Encoding", "gzip,deflate");
		   connection
		     .setRequestProperty(
		       "User-Agent",
		       "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.101 Safari/537.36");
		   connection.connect();
		   @SuppressWarnings("unused")
		String charset="UTF-8";
		   String encoding=connection.getHeaderField("Content-Type");
		   ArrayList<String> temp=RegexString(encoding, "charset=(.*+)");
		   temp.add("0");
		   String type=temp.toArray(new String[0])[0];//.toString();
		  // System.out.println( type);
		   if(type=="0")type="UTF-8";
		   InputStream beforeDecompress = connection.getInputStream();
		   try{
			   GZIPInputStream afterDecompress = new GZIPInputStream(beforeDecompress);
			   in = new BufferedReader(new InputStreamReader(afterDecompress,type)); //尝试用gzip解压，失败则直接解压
					   // 用来临时存储抓取到的每一行的数据
		   }catch(Exception e3){
			 
			   in = new BufferedReader(new InputStreamReader(beforeDecompress,type));
		   }
		   
		   // 初始化 BufferedReader输入流来读取URL的响应
		  
		   String line;
		   while ((line = in.readLine()) != null) {
		    // 遍历抓取到的每一行并将其存储到result里面
		    result += line+"\n";
		   }


		   try {
		    if (in != null) {
		     in.close();
		    }
		   } catch (Exception e2) {
		    e2.printStackTrace();
		   }
		  return result;
		 }
	 public static String SendGet(String url,String type) throws IOException {
		 trustAllHosts();
		  // 定义一个字符串用来存储网页内容
		  String result = "";
		  // 定义一个缓冲字符输入流
		  BufferedReader in = null;
		   // 将string转成url对象
		   URL realUrl = new URL(url);
		   // 初始化一个链接到那个url的连接
		  // URLConnection connection =realUrl.openConnection();
		   HttpURLConnection connection =(HttpURLConnection) realUrl.openConnection();
		   // 开始实际的连接
		  // connection.setRequestProperty("Accept-Encoding", "gzip,deflate");
		   connection
		     .setRequestProperty(
		       "User-Agent",
		       "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.101 Safari/537.36");
			  // Post
			  connection.setDoOutput(true);
			  connection.setDoInput(true);
			  connection.connect();
		   InputStream beforeDecompress = connection.getInputStream();
		   try{
			   GZIPInputStream afterDecompress = new GZIPInputStream(beforeDecompress);
			   in = new BufferedReader(new InputStreamReader(afterDecompress,type)); //尝试用gzip解压，失败则直接解压
					   // 用来临时存储抓取到的每一行的数据
		   }catch(Exception e3){
			 
			   in = new BufferedReader(new InputStreamReader(beforeDecompress,type));
		   }
		   
		   // 初始化 BufferedReader输入流来读取URL的响应
		  
		   String line;
		   while ((line = in.readLine()) != null) {
		    // 遍历抓取到的每一行并将其存储到result里面
		    result += line+"\n";
		   }
		  // 使用finally来关闭输入流
		  
		   try {
		    if (in != null) {
		     in.close();
		    }
		   } catch (Exception e2) {
		    e2.printStackTrace();
		   }

		  return result;
		 }
	 static ArrayList<String> RegexString(String targetStr, String patternStr) {
		  // 预定义一个ArrayList来存储结果
		  ArrayList<String> results = new ArrayList<String>();
		  // 定义一个样式模板，此中使用正则表达式，括号中是要抓的内容
		  Pattern pattern = Pattern.compile(patternStr);
		  // 定义一个matcher用来做匹配
		  Matcher matcher = pattern.matcher(targetStr);
		  // 如果找到了
		  boolean isFind = matcher.find();
		  while (isFind) {
		   //添加成功匹配的结果
		   results.add(matcher.group(1));
		   // 继续查找下一个匹配对象
		   isFind = matcher.find();
		  }
		  return results;
		 }
	 public static void updateStationList() throws UnsupportedEncodingException{
		 String url="https://kyfw.12306.cn/otn/lcxxcx/init";
		 String result;
		 try{
		 result=SendGet(url,"utf-8");
		 ArrayList<String> url2= RegexString(result,"station_version=([^\"]*)\"");
		// System.out.println(url2);
		 //result=RegexString(result,"|([^\"]*)\"}").toString();
		// ArrayList<String> target= RegexString(result,"async src=\"([^\"]*)");
		 url="https://kyfw.12306.cn/otn/resources/js/framework/station_name.js?"+url2.get(0);
		 result=SendGet(url,"utf-8");}
		 catch (IOException e) {
			 JOptionPane.showMessageDialog(null,"更新信息失败！");
			 return;
		}
		 File file = new File("station.txt");
	        try {  
	            file.createNewFile(); // 创建文件  
	        } catch (IOException e) {  
	            // TODO Auto-generated catch block  
	            e.printStackTrace();  
	        }  
	        
	       // byte bt[] = new byte[1024];  
	      //  bt = result.getBytes();  
	        try {  
	        	BufferedWriter in =new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
	            try {  
	                in.write(result);  
	                in.close();  
	                // boolean success=true;  
	                JOptionPane.showMessageDialog(null,"更新信息成功！");  
	            } catch (IOException e) {  
	                // TODO Auto-generated catch block  
	                e.printStackTrace();  
	            }  
	        } catch (FileNotFoundException e) {  
	            // TODO Auto-generated catch block  
	            e.printStackTrace();  
	        }
		 //System.out.println(result);
	 }
	 public static String readTxtFile(String filePath,String encoding){
		 String result="";

	        try {
	                File file=new File(filePath);
	                if(file.isFile() && file.exists()){ //判断文件是否存在
	                    InputStreamReader read = new InputStreamReader(
	                    new FileInputStream(file),encoding);//考虑到编码格式
	                    BufferedReader bufferedReader = new BufferedReader(read);
	                    String lineTxt = null;
	                    while((lineTxt = bufferedReader.readLine()) != null){
	                        result+=lineTxt;
	                    }
	                    read.close();
	        }else{
	        	JOptionPane.showMessageDialog(null,"找不到车站信息文件！请更新车站信息！");
	        	return "";
	        }
	        } catch (Exception e) {
	        	JOptionPane.showMessageDialog(null,"文件读取出错，请更新车站信息！");
	            e.printStackTrace();
	        }
	        return result;
	     
	    }
	 public static String getStationName(String name){
		 String result;
		 String get=readTxtFile("station.txt","UTF-8");
		 if (get.length()==0)return "";
		 try{
			 result=RegexString(get,name+"[^\u2E80-\u9FFF]([A-Z]*)").get(0);
			 if (result.length()==0) throw new Exception();
		 }
		 catch(Exception e){
			 result="";
			 JOptionPane.showMessageDialog(null,"找不到该车站！");
		 }
		// System.out.println(result);
		 //System.out.println(get);
		 return result;
	 }
	public static String finishSearchUrl(String fromstation,String tostation,String date, String type){
		String url="https://kyfw.12306.cn/otn/leftTicket/queryA?leftTicketDTO.train_date="+date+"&leftTicketDTO.from_station=";
		String aString=getStationName(fromstation);
		if(aString.length()==0) return "";
		String bString=getStationName(tostation);
		if(bString.length()==0) return "";
		return url+aString+"&leftTicketDTO.to_station="+bString+"&purpose_codes="+type;

	}
	 public static ArrayList<String> getTrainList(String data){
		ArrayList<String> trainlist=RegexString(data,"\"station_train_code\":\"([^\"]+)");
		return trainlist;
	 }
	 public static String getTrainInfo(String train_num,String type,String data){//type为需求的票的类型 ticket_type=["swz_num","tz_num","zy_num","ze_num","gr_num","rw_num","yw_num","rz_num","yz_num","wz_num"]  #swz商务座 zy一等座 ze二等座 wz无座 yz硬座 rw软卧 yw硬卧
	        // #tz特等座 gr高级软卧 yz硬座
		 try{
			 String temp=RegexString(data,"("+train_num+"\",\"[^}]+})").get(0);
			 return RegexString(temp,type+"\":\"([^\"]+)\"").get(0);}
		 catch(Exception e ){
			 System.out.println(e.getStackTrace());
			 JOptionPane.showMessageDialog(null,"找不到该车次信息");
		 }
			return "";
		 }
	
	/*public static void main(String[] args) {
		  // 定义即将访问的链接
		//  String url = "http://blog.sina.com.cn/s/blog_5f99653f01016yr8.html";
		  // 访问链接并获取页面内容
		//  String result = SendGet(url);
		  // 使用正则匹配图片的src内容
		 // ArrayList<String> imgSrc = RegexString(result,"img src=\"([^\"]*)");//"href=\"[^\"]?");//"img src=\"(.+?)\"");
		  // 打印结果
		 //updateStationList();
		//getStationName("北京北");
		  //System.out.println(imgSrc);
		//TicketMonitor mainWindow = new TicketMonitor();
        //mainWindow.setVisible(true);
		// updateStationList();
		Test te=new Test();//创建一个对象te
		te.setUI();//调用setUI()函数，显示UI；
		//String date=dc.getDate().toInstant().toString().substring(0,10);
		//te.RunTask();//如果调用该函数出现空指针异常
		 bt1.addMouseListener(new MouseAdapter() {
			 public void mousePressed(MouseEvent e) {
				 // ***** 检查各个参数情况，调用相应接口查询 *****
				 String type1 = null;//乘客类型
				 if(rb1.isSelected())
					 type1="ADULT";
				 if(rb2.isSelected())
					 type1="0X00";
				 String fromSta=tf1.getText();//出发站
				 String toSta=tf2.getText();//终止站
				 String date=dc.getDate().toInstant().toString().substring(0,10);//获得日期
				 String url=finishSearchUrl(fromSta,toSta,date,type1);
				 System.out.println(url);
				 String result = SendGet(url);
				 ArrayList<String> s2=getTrainList(result);
				 //System.out.println(s2);
			//	 ArrayList<String> imgSrc = RegexString(result,"img src=\"([^\"]*)");//"href=\"[^\"]?");//"img src=\"(.+?)\"");
				// getTrainList
				 String str="";//str保存输出内容
				 for(String s:s2){//遍历s2字符串列表保存的车次序列
					 String s0=s;
					 s0=s0+'\t';
					 try{s0=s0+" "+getTrainInfo(s,"swz_num",result);
					 
					 }catch(Exception e1){
						 System.out.println(e1);
					 }
					 try{s0=s0+" "+getTrainInfo(s,"tz_num",result);
					 
					 }catch(Exception e2){
						 System.out.println(e2);
					 }
					 try{s0=s0+" "+getTrainInfo(s,"zy_num",result);
					 
					 }catch(Exception e3){
						 System.out.println(e3);
					 }
					 try{s0=s0+" "+getTrainInfo(s,"ze_num",result);
					 
					 }catch(Exception e4){
						 System.out.println(e4);
					 }
					 try{s0=s0+" "+getTrainInfo(s,"gr_num",result);
					 
					 }catch(Exception e5){
						 System.out.println(e5);
					 }
					 try{s0=s0+" "+getTrainInfo(s,"rw_num",result);
					 
					 }catch(Exception e6){
						 System.out.println(e6);
					 }
					 try{s0=s0+" "+getTrainInfo(s,"yw_num",result);
					 
					 }catch(Exception e7){
						 System.out.println(e7);
					 }
					 try{s0=s0+" "+getTrainInfo(s,"rz_num",result);
					 
					 }catch(Exception e8){
						 System.out.println(e8);
					 }
					 try{s0=s0+" "+getTrainInfo(s,"yz_num",result);
					 
					 }catch(Exception e9){
						 System.out.println(e9);
					 }
					 try{s0=s0+" "+getTrainInfo(s,"wz_num",result);
					 
					 }catch(Exception e10)
					 {
						 System.out.println(e10);
					 }
					 str+=s0+'\n';
				 }
				 TextField.setText(str);//在TestField显示字符串
			}
		});
		 //type为需求的票的类型 ticket_type=["swz_num","tz_num","zy_num","ze_num","gr_num","rw_num","yw_num","rz_num","yz_num","wz_num"]  #swz商务座 zy一等座 ze二等座 wz无座 yz硬座 rw软卧 yw硬卧
			        // #tz特等座 gr高级软卧 yz硬座
		/*String smtp = mPanel1.getText();  //smtp地址
	    String from = mPanel2.getText();  //发件人
	    String to = mPanel4.getText();  //收件人
	    String copyto = null;  
	    String subject = "邮件主题";  //邮件主题
	    String content="";  //邮件内容
	    String username="用户名";  
	    String password=mPanel3.getText();  //密码
	    String filename = "info.txt";  //附件
	    String timeLine=mPanel5.getText();//监测间隔
	    long intTime=Long.parseLong(timeLine)*60*1000;
	    String train=mPanel6.getText();//监测车次
	    
	    try{
	    	Mail.sendAndCc(smtp, from, to, copyto, subject, content, username, password, filename);  
	    }catch(Exception e8)
	    {
	    	System.out.println(e8);
	    }*/
		
       /* bt5.addMouseListener(new MouseAdapter() {//感觉这个函数按钮没有用。。。
            @Override
            public void mousePressed(MouseEvent e) {
                // ***** SMTP测试相关接口调用 *****

            }
        });
        bt6.addMouseListener(new MouseAdapter() {//重置监测内容
            @Override
            public void mousePressed(MouseEvent e) {
                mtf1.setText("");
                mtf2.setText("");
                mtf3.setText("");
                mtf4.setText("");
                mtf5.setText("");
                mtf6.setText("");
            }
        });
    }

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}*/
}
