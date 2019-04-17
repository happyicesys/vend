package com;

public class clsConst {

	public static final byte START_SIGN1 = 'E';
	public static final byte START_SIGN2 = 'F';
	public static final int MAX_DATA_LENGTH = 1024;
	
	public static final int TRADE_OBJ_SIZE=34;
	public static final int TIME_OBJ_SIZE = 8;
	
	public static final String CHAR_CODE="GBK";
	
	
	
	
	
	public static final int HTTP_INVALID_TYPE 			=0x7f;
	public static final int HTTP_ERR_TYPE 				=0x7e;

	public static final int HTTP_EMPTY_OBJ 			=0;
	public static final int HTTP_CFG_INFO				=1;
	public static final int HTTP_COL_LIST_DATA			=2;
	public static final int HTTP_COL_LIST_NAME			=3;
	public static final int HTTP_CARD_INFO				=4;
	public static final int HTTP_COL_STOCK				=5;
	public static final int HTTP_COL_CAPACITY			=6;
	public static final int HTTP_MDB_DATA				=7;
	public static final int HTTP_SYC_TIME		    	=8;
	public static final int HTTP_TRADE_DATA			    =9;
	public static final int HTTP_MECHINE_DATA			=10;
	public static final int HTTP_KEY_DATA				=11;
	public static final int HTTP_DOWN_FILE				=12;
	public static final int HTTP_ACTIVE				=13;
	
	public static final int HTTP_DO_ACTIVE			=14;

	public static final int HTTP_UPDATE_GT_FIRM = 15;
	
	
	public static final String SLOT_FORMAT="%03d";
	public static final String SLOT_HEX_FORMAT="%3X";
	public static final int _TYPE_BRD_MINI=1;
	public static final int _TYPE_BRD_60SLOT=2;
	
	
	public static final int ERR_NO_ERR						=0;
	public static final int ERR_OVER_INDEX					=1;
	public static final int ERR_MOTOR_CLOSE_LEVEL_ABNORMAL	=2;
	public static final int ERR_MOTOR_CANT_ROUND			=3;
	public static final int ERR_MOTOR_DRIVER_BREAK			=4;
	public static final int ERR_MOTOR_DRIVER_SHORT			=5;
	public static final int ERR_MOTOR_ROUND_OVER_TIME		=6;
	public static final int ERR_LIST_IS_FULL				=7;
	public static final int ERR_INVALID_OBJECT				=8;
	public static final int ERR_CANT_FIND_USERDATA			=9;
	public static final int ERR_CHK_READER					=10;
	public static final int ERR_NO_CARD 					=11;
	public static final int ERR_INVALID_PARA				=12;
	public static final int ERR_FAIL_LOAD_PWD				=13;
	public static final int ERR_READ_DATA					=14;
	public static final int ERR_CARD_PWD_WRONG				=15;
	public static final int ERR_WRT_DATA					=16;
	public static final int ERR_BALANCE_DATA				=17;
	public static final int ERR_VAL_DATA					=18;
	public static final int ERR_CARD_NOT_INIT				=19;
	public static final int ERR_NOT_ENOUGH_MONEY			=20;
	public static final int ERR_CARD_PROTECT_BLOCK			=21;
	public static final int ERR_SLOT_FAULT					=22;
	public static final int ERR_SLOT_NO_STOCK				=23;
	public static final int ERR_SLOT_INHIBIT				=24;
	public static final int ERR_SLOT_INVALID				=25;
	public static final int ERR_LCD_OVER_X					=26;
	public static final int ERR_LCD_OVER_Y					=27;
	public static final int ERR_RECEIVE_DATA_ERR			=28;
	public static final int ERR_CANT_FIND_SYSDATA			=29;
	public static final int ERR_QUEUE_FULL					=30;
	public static final int ERR_QUEUE_EMPTY					=31;
	public static final int ERR_GPRS_RET_OVERTIME			=32;
	public static final int ERR_GPRS_RET_OVER_FLOW			=33;
	public static final int ERR_GPRS_RET_ERROR				=34;
	public static final int ERR_GPRS_OTHER_ERROR			=35;
	public static final int ERR_MICRO_SW_FAULT				=36;
	public static final int ERE_VP_DATACHK_ERR				=37;
	public static final int ERR_VP_STARTSIGN_ERR			=38;
	public static final int ERR_VP_VER_ERR					=39;
	public static final int ERR_VP_NO_HOST_CONNECT			=40;
	public static final int ERR_LOOP_OVER_TIME				=41;
	public static final int ERR_BAUD_NOT_CORRECT			=42;
	public static final int ERR_TIME_OVER					=43;
	public static final int ERR_CRC							=44;
	public static final int ERR_FRAME_ID					=45;
	public static final int ERR_BREAK_DRIVER_LINK			=46;
	public static final int ERR_BREAK_PC_LINK				=47;
	public static final int ERR_BREAK_SERVER_LINK			=48;
	public static final int ERR_MDB_NAK						=49;
	public static final int ERR_MDB_CHK_DC					=50;
	public static final int ERR_MDB_NO_CHANGER				=51;
	public static final int ERR_MDB_NO_BILLACCEPTOR			=52;
	public static final int ERR_DATA_INDEX_OVER_FLOW		=53;
	public static final int ERR_SECTION_NUM					=54;
	public static final int ERR_FLASH_READY					=55;
	public static final int ERR_FLASH_WRITE					=56;
	public static final int ERR_FLASH_ERASE					=57;
	public static final int ERR_NOT_BLANK_BLOCK				=58;
	public static final int ERR_DATA_SIZE					=59;
	public static final int ERR_MEM_BOUND					=60;
	public static final int ERR_BEEP						=61;
	public static final int ERR_NO_CARD_REC					=62;
	public static final int ERR_DATA_AERA_FULL				=63;
	public static final int ERR_CARD_EXIST					=64;
	public static final int ERR_HTTP_DATA_NOT_IN			=65;
	public static final int ERR_HTTP_DATA_ERROR				=66;
	public static final int ERR_FRAME_ID_NOT_MATCH			=67;
	public static final int ERR_WRONG_KEY					=68;
	public static final int ERR_PUB_KEY						=69;
	public static final int ERR_NULL_KEY					=70;
	
	public static final int ERR_UNKOWN						=0xf0;
	public static final int ERR_NO_SLOT_IS_OK           	=0xfe;

	public static final String DEFAULT_CHAR_CODE = "GBK";
	public static final String SLOT_FORMAT_HEX = "%X";	
	public static final String SLOT_FORMAT_DEC = "%d";
	public static final int ACT_LOAD_GOODS = 1;
	
	public static final int TRADE_TYPE_NO_LIMIT = -1;
	public static final int TRADE_TYPE_CASH = 0;
	public static final int TRADE_TYPE_CARD = 1;
	public static final int TRADE_TYPE_BANK= 2;
	public static final int TRADE_TYPE_SANFU= 3;
	public static final int TRADE_TYPE_AL_QR = 4;
	public static final int TRADE_TYPE_AL_SOUND = 5;
	public static final int TRADE_TYPE_WX_QR = 6;
	public static final int TRADE_TYPE_GOODS_CODE = 7;
	
	public static final int TRADE_TYPE_BAIDU = 8;
	public static final int TRADE_TYPE_FETCH_GOODS_CODE = 9;
	public static final int TRADE_TYPE_COCO = 10;  
	public static final String[] TRADE_TYPE_DES=new String[]{
		"Cash",
		"Cashless",
		"UnionPay",
		"FlashPay",
		"AlipayScan",
		"AlipayGenerate",
		"WchatScan",
		"GiftCode",
		"BaiduPay",
		"RetrieveCode",
		"FreeVend"
	};
	
	public static final int TABLE_CMD_SLOT= 1;
	public static final int TABLE_CMD_VENDER= 2;
	public static final int TABLE_CMD_TRADE= 3;
	public static final int TABLE_CMD_TRADE_COMPARE= 4;
	
	public static final int TABLE_CMD_TRADE_TYPE_AL_QR = 5;			/*扫码支付*/
	public static final int TABLE_CMD_TRADE_TYPE_GET_CODE = 6;	/*取货码*/
	public static final int TABLE_CMD_TRADE_TYPE_WX_QR = 7;			/*微信支付*/
	public static final int TABLE_CMD_PUSH_SLOT = 8;
	public static final int TABLE_CMD_TELL_AL_QR_INVALID_PAID=9;


	public static final int TABLE_CMD_TELL_AL_SOUND_INVALID_PAID=10;
	public static final int TABLE_CMD_TELL_WX_QR_INVALID_PAID = 11;
	public static final int TABLE_CMD_SYNC_GOODS=12;
	
	public static final int TABLE_CMD_GET_QR_CODE=13;	
	
	public static final int TABLE_CMD_CONNECT = 0xff;
	public static final int TABLE_CMD_HEART = 0xfe;
	

	
	
	public static final int HTTP_GET=0x80;
	public static final int HTTP_SET=0x00;
	public static final String NAK = "ERROR";
	public static final String ACK = "OK";
	
	public static final String POWER_USER_NAME = "andy_he";
	
	
	/*2013-12-15 23:50:00
	 * 文件下载时，确定是下载文件信息，还是下载文件内容
	 * */
	public static final int FILE_DOWN_ACT_DOWN_FILE = 0;
	public static final int FILE_DOWN_ACT_FILE_INFO = 1;
	
	/*2013-12-15 23:42:05 
	 * 用于
	 * */
	public static final int ACTION_SET_STOCK=1;
	public static final int ACTION_SET_CAPACITY=2;
	public static final int ACTION_SET_DISCOUNT=3;
	public static final int ACTION_SET_PRICE=4;
	
	public static final int ACTION_SET_VENDER_CAN_USE=5;
	
	public static final int ACTION_FILL_STOCK=6;/*快捷上货*/
	
	
	public static final int ACTION_JSON_UPDATE_PORT = 7;/*通过json来更新货道列表*/
	
	
	public static final int ACTION_JSON_GET_USER = 8;/*通过json来获取用户信息*/
	
	public static final int ACTION_JSON_UPDATE_USER = 9;/*通过json来更新用户*/
	
	public static final int ACTION_JSON_ADD_USER = 10;/*通过json来添加用户*/
	
	public static final int ACTION_JSON_DELETE_USER = 11;/*删除用户信息*/
	
	public static final int ACTION_JSON_GET_GROUPLST = 12;/*获取集团列表信息*/
	
	public static final int ACTION_FRESH_VENDER_PARA = 13;/*清除机器在服务器端的参数缓存，使其在数据库中重新读取*/
	
	public static final int ACTION_TEST_WX_QRCODE_GET = 14;/*测试微信二维码的获取功能*/
	public static final int ACTION_TEST_AL_QRCODE_GET = 15;/*测试支付宝二维码的获取功能*/
	public static final int ACTION_TEST_WX_SEND_MSG = 16;/*微信公众号发送消息测试功能*/
	public static final int ACTION_JIESUAN= 17;/*结算交易记录*/
	public static final int ACTION_SAVE_LOG= 18;/*强制保存通信日志*/
	public static final int ACTION_MANUAL_TRANSFER= 19;/*强制转款*/
	/*2013-12-15 23:43:42
	 * 添加货道时，允许添加的最大个数
	 * */
	public static final int MAX_PORT_COUNT=100;
	
	
	/*客户端发起访问时，产生的错误的等级*/
	public static final int ERR_LEVEL_NO_ERR = 0;
	public static final int ERR_LEVEL_OPERATION_ERR = 1;
	public static  final int ERR_LEVEL_NO_OPERATION_RIGHT=2;
	public static final int ERR_LEVEL_CANT_ACCESS = 3;
	

	public static final int TRADE_STATUS_WAIT_PAY =0;	
	public static final int TRADE_STATUS_WAIT_TRANSFOR =1;
	public static final int TRADE_STATUS_WAIT_TRANSFOR_RET =2;
	
	
	public static final int TRADE_FAIL_TO_PAY =-1;	
	public static final int TRADE_NEED_TRANSFOR =1;
	public static final int TRADE_DOING_TRANSFOR =2;
	public static final int TRADE_TRANSFOR_COMPLETE =3;
	
	public static final int FRAME_START_FLG = (int)'!';
	public static final int FRAME_END_FLG = (int)'\\';
	
	
	public static final String WX_SUCCESS_CODE = "SUCCESS";
	public static final String WX_FAIL_CODE = "FAIL";
	
	public static final int MAX_MOTOR_COUNT_PER_MINI_BRD = 12;
	
	public static final int MAX_MOTOR_COUNT_PER_BRD = 60;
	public static final int TRADE_STATUS_COPED = 1;
	
	public static final int WX_CMD_ID_LOOK_TRADE_OF_TODAY = 1;
	public static final int WX_CMD_ID_LOOK_TRADE_OF_THIS_MONTH = 2;	
	public static final int WX_CMD_ID_LOOK_VENDER_STATE = 3;
	public static final int WX_CMD_ID_LOOK_SLOT_OUT_STATE = 4;
	public static final int WX_CMD_ID_LOOK_SLOT_ERR_STATE = 5;
	
	public static final int WX_CMD_ID_GET_OPENID = 999;

	public static int[] valid_slot_id=new int[]{  
			0xA0,0xA1,0xA2,0xA3,0xA4,0xA5,0xA6,0xA7,0xA8,0xA9,
			0xB0,0xB1,0xB2,0xB3,0xB4,0xB5,0xB6,0xB7,0xB8,0xB9,
			0xC0,0xC1,0xC2,0xC3,0xC4,0xC5,0xC6,0xC7,0xC8,0xC9,
			0xD0,0xD1,0xD2,0xD3,0xD4,0xD5,0xD6,0xD7,0xD8,0xD9,
			0xE0,0xE1,0xE2,0xE3,0xE4,0xE5,0xE6,0xE7,0xE8,0xE9,
			0xF0,0xF1,0xF2,0xF3,0xF4,0xF5,0xF6,0xF7,0xF8,0xF9,
			
			0x101,0x102,0x103,0x104,0x105,0x106,0x107,0x108,0x109,0x110,
			0x111,0x112,0x113,0x114,0x115,0x116,0x117,0x118,0x119,0x120,
			0x121,0x122,0x123,0x124,0x125,0x126,0x127,0x128,0x129,0x130,
			0x131,0x132,0x133,0x134,0x135,0x136,0x137,0x138,0x139,0x140,
			0x141,0x142,0x143,0x144,0x145,0x146,0x147,0x148,0x149,0x150,
			0x151,0x152,0x153,0x154,0x155,0x156,0x157,0x158,0x159,0x160,
			
			0x161,0x162,0x163,0x164,0x165,0x166,0x167,0x168,0x169,0x170,
			0x171,0x172,0x173,0x174,0x175,0x176,0x177,0x178,0x179,0x180,
			0x181,0x182,0x183,0x184,0x185,0x186,0x187,0x188,0x189,0x190,
			0x191,0x192,0x193,0x194,0x195,0x196,0x197,0x198,0x199,0x200,
			0x201,0x202,0x203,0x204,0x205,0x206,0x207,0x208,0x209,0x210,
			0x211,0x212,0x213,0x214,0x215,0x216,0x217,0x218,0x219,0x220,

		};
	
}
