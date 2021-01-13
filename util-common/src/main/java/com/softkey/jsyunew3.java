package com.softkey;

public class jsyunew3
{
	//�����İ汾
	public static native int GetVersion(String InPath);
	//��������չ�汾
	public static native int GetVersionEx(String InPath);
	//������ID
	public static native long GetID_1(String InPath);
	public static native long GetID_2(String InPath);
	//�������Ĵ�����Ϣ
	public static native long get_LastError();
	//���Ҽ�����
	public static native String FindPort(int start);

	//���ö�����
	public static native int SetReadPassword(String W_hkey, String W_lkey, String new_hkey, String new_lkey, String InPath);
	//����д����
	public static native int SetWritePassword(String W_hkey, String W_lkey, String new_hkey, String new_lkey, String InPath);

	//�Ӽ������ж�ȡһ���ֽ�
	public static native int YReadEx(short Address, short len, String HKey, String LKey, String InPath);
	//�Ӽ������ж�ȡһ���ֽڣ�һ�㲻ʹ��
	public static native int YRead(short Address, String HKey, String LKey, String InPath);
	//�ӻ������л������
	public static native short GetBuf(int pos);
	//дһ���ֽڵ���������
	public static native int YWriteEx(short Address, short len, String HKey, String LKey, String InPath);
	//дһ���ֽڵ��������У�һ�㲻ʹ��
	public static native int YWrite(short inData, short Address, String HKey, String LKey, String InPath);
	//����Ҫд��Ļ�����������
	public static native int SetBuf(int pos, short Data);
	//�Ӽ������ж��ַ���-��
	public static native String NewReadString(int Address, int len, String HKey, String LKey, String InPath);
	//д�ַ�������������-��
	public static native int NewWriteString(String InString, int Address, String HKey, String LKey, String InPath);
	//���ݾɵĶ�д�ַ�������������ʹ��
	public static native String YReadString(short Address, short len, String HKey, String LKey,String InPath);
	public static native int YWriteString(String InString, short Address, String HKey, String LKey,String InPath);
	//'������ǿ�㷨��Կһ
	public static native int SetCal_2(String Key, String InPath);
	//ʹ����ǿ�㷨һ���ַ������м���
	public static native String EncString(String InString, String InPath);
	//ʹ����ǿ�㷨һ�Զ��������ݽ��м���
	public static native int Cal(String InPath);

	//ʹ����ǿ�㷨���ַ������н���
	public static native String DecString(String InString, String Key);
	//����Ҫ���ܵĻ�����������
	public static native int SetEncBuf(int pos, short Data);
	//�ӻ������л�ȡ���ܺ������
	public static native short GetEncBuf(int pos);
	
	//���ؼ������Ĺ�Կ��
	public static native String GetPubKeyX(String InPath);
	public static native String GetPubKeyY(String InPath);
	//����SM2��Կ�ԣ���ȡ˽Կ����Կ
	public static native String get_GenPriKey();
	public static native String get_GenPubKeyX();
	public static native String get_GenPubKeyY();
	//����SM2��Կ��,������Կ��
	public static native int StarGenKeyPair(String InPath);
	public static native String SM2_EncString(String InString,String InPath);
	public static native String SM2_DecString(String InString,String Pin,String InPath);
	public static native int YtSetPin(String OldPin,String NewPin,String InPath);
	//����Ϣ����ǩ��
	public static native String YtSign(String msg,String Pin,String InPath);
	//��ǩ��������֤
	public static native boolean YtVerfiy(String id,String msg,String PubKeyX,String  PubKeyY,String VerfiySign,String InPath);
	//����SM2��Կ�Լ����
	public static native int Set_SM2_KeyPair(String PriKey,String PubKeyX,String PubKeyY,String sm2UserName,String InPath);
	//��ȡ�������е����
	public static native String GetSm2UserName(String InPath);
	//��������Ӳ��оƬΨһID
	public static native String GetChipID(String InPath);
	//���Ҽ����������ص���U�̵�·��,��U�̵��̷���ͨ�����·��Ҳ����ֱ�Ӳ�����
	public static native String FindU(int start);
	//����ָ���ļ�������ʹ����ͨ�㷨���������ص���U�̵�·��,��U�̵��̷���ͨ�����·��Ҳ����ֱ�Ӳ�����
	public static native String FindU_3(int start,int in_data,int verf_data);
	//����ָ���ļ�������ʹ����ͨ�㷨һ�������ص���U�̵�·��,��U�̵��̷���ͨ�����·��Ҳ����ֱ�Ӳ�����
	public static native String FindU_2(int start,int in_data,int verf_data);
	//����U�̲���Ϊֻ��״̬��
	public static native int SetUReadOnly(String InPath);
	//�����Ƿ���ʾU�̲����̷�����Ϊ��ʾ����Ϊ����ʾ
	public static native int SetHidOnly(boolean IsHidOnly,String InPath);
	//����U�̲����Ƿ�Ϊֻ��״̬����Ϊֻ��
	public static native boolean IsUReadOnly(String InPath);

	//***��ʼ������������
	public static native int ReSet(String Path);

	static
     {
		System.load("/usr/bin/libJsyunew3_64.so");
         //System.loadLibrary("Jsyunew3");
     }
 
}