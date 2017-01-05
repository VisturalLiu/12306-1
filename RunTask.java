package getTicket;

import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class RunTask extends Thread{
	public boolean tag;
	public static String type;
	public static String fromSta;
	public static String toSta;
	public static String date;
	public static String smtp;
	public static String from;
	public static String to;
	public static String copyto=null;
	public static String subject = "邮件主题";
	public static String password;
	public static String timeLine;
	public static String train,ticketType[] ={"swz_num","tz_num","zy_num","ze_num","gr_num","rw_num","yw_num","rz_num","yz_num","wz_num"};
	public static String ticketConvertType[]={"商务座","特等座","一等座","二等座","高级软卧","软卧","硬卧","软座","硬座"};
	public static boolean selectedType[] = new boolean[10];
	static long intTime;
	String content="";
	public static void getData(){
		 type = TicketMonitor.type;//乘客类型
   		 fromSta=TicketMonitor.fromSta;//出发站
   		 toSta=TicketMonitor.toSta;//目标站
   		 date=TicketMonitor.date;
		 smtp =TicketMonitor.smtp;  //获得smtp地址
		 from = TicketMonitor.senderMail;  //获得发件人
		 to = TicketMonitor.receiverMail;  //获得收件人
		 copyto = null; //抄送无null
		 subject = "车次信息";  //邮件主题
		password=TicketMonitor.pwd;  //密码
		 timeLine=TicketMonitor.interval;//监测间隔，分钟
		 if(!timeLine.equals(""))intTime=Long.parseLong(timeLine)*60*1000;//返回毫秒间隔
		 train=TicketMonitor.targetTrainNum;//监测车次
		 for(int i = 0; i < 10; i++)selectedType[i]=TicketMonitor.selectedType[i];
	}
	public static void testMail()
	{
		getData();
			try{
			    	//System.out.println(content);
				String content="这是一封测试邮件";
			    if(Mail.send(smtp, from, to, "测试", content, from, password)) {
			    	JOptionPane.showMessageDialog(null,"邮件发送成功！请检查邮箱确认！");//调用发送邮件接口
					TicketMonitor.bt3.setSelected(false);
				}
			    else  JOptionPane.showMessageDialog(null,"邮件发送失败！请检查网络及smtp设置！");
			 }catch(Exception e8)
			 {
				 JOptionPane.showMessageDialog(null,"请检查网络连接是否正常以及输入信息是否正确");
			 }
			
	}
	public void run()
	{
		getData();
		int flag=0,flag2=0;
		String s0="";
		String url=Test.finishSearchUrl(fromSta,toSta,date,type);
//		System.out.println(url);
		while(true)
		{
			content="";
			try{
				
				String result = Test.SendGet(url,"UTF-8");
//				System.out.println("Alive");
				for(int i=0;i<9;i++)
				{
//					System.out.println(selectedType[i]);
					if(selectedType[i])
					{
//						System.out.println(result);
						s0=Test.getTrainInfo(train,ticketType[i],result);
					//	System.out.println(s0);
						if(s0.length()>0&&(s0.charAt(0)=='有'||s0.charAt(0)>'0'&&s0.charAt(0)<='9'))
							{flag=1;
							flag2=1;}
						if(flag==1)
						{
							content=content+ticketConvertType[i]+"剩余情况："+s0+'\n';
							flag=0;
						}
					}
					
				}
				/*if(flag2==0)
				{
					content="不好意思，当前时间本车次所有座位类型均无票\n，谢谢您使用本系统";
					
				}
				*/
				if(flag2==1)
				{
					content=content+"您的查询结果已给出，谢谢您使用本系统";
					if(Mail.send(smtp, from, to,  subject, content, from, password))	{TicketMonitor.bt3.setSelected(false);TicketMonitor.mtip.setText("服务已停止");break;}
				}
				
				try{
					Thread.sleep(intTime);
				}catch(InterruptedException e)
				{
					e.printStackTrace();
					System.out.println("quit");
					break;
				}
				/*if(flag==1)
				{
					for(int i=0;i<10;i++)
					{
						try{
							content=content+" "+Test.getTrainInfo(train,ticketType[i],result);
						}catch(Exception e1){
							System.out.println(e1);
						}
					}
					Mail.send(smtp, from, to,  subject, content, from, password);
					break;
				}*/
			}catch(Exception e){}
			
		}
	}
		public  static void search(){
				getData();
				 String url=Test.finishSearchUrl(fromSta,toSta,date,type);
				if (url.length()==0) return;
				 System.out.println(url);
				 try{
				 String result = Test.SendGet(url,"UTF-8");
				 ArrayList<String> s2=Test.getTrainList(result);
				 //System.out.println(s2);
			//	 ArrayList<String> imgSrc = RegexString(result,"img src=\"([^\"]*)");//"href=\"[^\"]?");//"img src=\"(.+?)\"");
				// getTrainList
				String str="车次\t商 务 座\t特 等 座\t一 等 座\t 二 等 座 \t高 级 软 卧 \t软  卧 \t硬   卧 \t 软  座 \t 硬  座\t无  座\n";//str保存输出内容
				 for(String s:s2){//遍历s2字符串列表保存的车次序列
					 String s0=s;
					 s0=s0+'\t';
					 for(int i=0;i<10;i++)
					 {
						 try{
							 s0=s0+" "+Test.getTrainInfo(s,ticketType[i],result)+'\t';
					 
						 }catch(Exception e1){
							 System.out.println(e1);
						 }
					}
					str+=s0+'\n';
				 }
				 TicketMonitor.TextField.setText(str);//在TestField显示字符串
			 }catch(IOException e){
				 JOptionPane.showMessageDialog(null,"查询失败！请重试或检查网络连接！");
				 }
				
			}
}
