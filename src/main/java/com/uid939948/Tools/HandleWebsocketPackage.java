package com.uid939948.Tools;

import com.uid939948.Conf.MainConf;
import com.uid939948.DO.BarrageHeadHandle;
import com.uid939948.Until.ByteUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import struct.JavaStruct;
import struct.StructException;

import java.nio.ByteBuffer;

public class HandleWebsocketPackage {


    private static Logger log = LogManager.getLogger(HandleWebsocketPackage.class);


    /**
     * 处理父数据包 单线程引用写法
     *
     * @param message
     */
    @SuppressWarnings("unused")
    public static void handle_Message(ByteBuffer message) throws Exception {
//		StringBuilder stringBuilder = new StringBuilder();
        byte[] bytes = ByteUtils.decodeValue(message);
        byte[] bs = null;
        String resultStr = null;
        BarrageHeadHandle barrageHeadHandle = null;
        if (bytes.length > 0) {
            barrageHeadHandle = unBEhandle(bytes);
            int data_len = barrageHeadHandle.getPackageLength();
            int head_len = barrageHeadHandle.getPackageHeadLength();
            int data_ver = barrageHeadHandle.getPackageVersion();
            int data_type = barrageHeadHandle.getPackageType();
            int data_other = barrageHeadHandle.getPackageOther();
//			log.info("数据包:" + "(" + data_len + "," + head_len + "," + data_ver + "," + data_type + ","
//					+ data_other + ")");
            bs = ByteUtils.subBytes(bytes, head_len, data_len - head_len);
//			resultStr =HexUtils.toHexString(bs);


//            log.info("开始处理响应数据");
//            log.info("响应数据类型为 " + data_ver);

            if (data_ver == 2) {
                if (data_type == 5) {
//                    log.info("收到消息 " + data_ver);
                    HandleWebsocketPackage.handle_zlibMessage(ByteUtils.BytesTozlibInflate(bs));
                }
//				else {
//					resultStr = HexUtils.toHexString(bs);
//					LOGGER.info("！！！！！！！！！！未知数据(1)v:" + data_ver + "t:" + data_type + ":" + resultStr);
//				}
            } else if (data_ver == 1) {
                if (data_type == 3) {
                    try {
                        //房间人气


//						PublicDataConf.ROOM_POPULARITY = ByteUtils.byteslong(bs);

                    } catch (Exception e) {
                        // TODO 自动生成的 catch 块
                        e.printStackTrace();
                    }
                } else if (data_type == 8) {
                    // 返回{code 0} 验证头消息成功后返回
//					try {
//						resultStr = new String(bs, "utf-8");
//					} catch (Exception e) {
//						// TODO 自动生成的 catch 块
//						e.printStackTrace();
//					}
//					LOGGER.info("服务器验证信息返回:"+resultStr);
                }
//				else {
//					resultStr = HexUtils.toHexString(bs);
//					LOGGER.info("！！！！！！！！！！未知数据(1)v:" + data_ver + "t:" + data_type + ":" + resultStr);
//				}
            } else if (data_ver == 0) {
                try {
                    resultStr = new String(bs, "utf-8");

					MainConf.resultStrs.add(resultStr);
					if (MainConf.parseMessageThread != null && !MainConf.parseMessageThread.FLAG) {
						synchronized (MainConf.parseMessageThread) {
                            MainConf.parseMessageThread.notify();
						}
					}

                } catch (Exception e) {
                    // TODO 自动生成的 catch 块
                    e.printStackTrace();
                }
//				resultStr = ByteUtils.unicodeToString(resultStr);	
            }
//			else {
//				resultStr = HexUtils.toHexString(bs);
//				LOGGER.info("！！！！！！！！！！未知数据(1):" + resultStr);
//			}
//			if (resultStr == null) {
//				LOGGER.info("空数据(1):"+resultStr);
//			}
        }
        bytes = null;
        resultStr = null;
    }

    /**
     * 处理解压后子数据包
     *
     * @param bytes
     */
    @SuppressWarnings("unused")
    public static void handle_zlibMessage(byte[] bytes) throws Exception {
        int offect = 0;
        String resultStr = null;
        int maxLen = bytes.length;
        byte[] byte_c = null;
        BarrageHeadHandle barrageHeadHandle = null;
        int data_len = 0;
        int head_len = 0;
        int data_ver = 0;
        int data_type = 0;
        int data_other = 0;
        byte[] bs = null;
//		int index = 1;
//		LOGGER.info(maxLen+"长的母数据包打印开始--------------------------------------------------------------------------------");
        while (offect < maxLen) {
            byte_c = ByteUtils.subBytes(bytes, offect, maxLen - offect);
            barrageHeadHandle = unBEhandle(byte_c);
            data_len = barrageHeadHandle.getPackageLength();
            head_len = barrageHeadHandle.getPackageHeadLength();
            data_ver = barrageHeadHandle.getPackageVersion();
            data_type = barrageHeadHandle.getPackageType();
            data_other = barrageHeadHandle.getPackageOther();
//			LOGGER.info("子包<"+index+">:"+"("+data_len+","+head_len+","+data_ver+","+data_type+","+data_other+")");
            bs = ByteUtils.subBytes(byte_c, head_len, data_len - head_len);
//			resultStr=HexUtils.toHexString(bs);
            if (data_ver == 2) {
                if (data_type == 5) {
//					resultStr = ByteUtils.BytesTozlibInflateString(bs);
////					resultStr = ByteUtils.unicodeToString(resultStr);
//					LOGGER.info("其他未处理消息(2):" + resultStr);
                }
//				else {
//					resultStr = HexUtils.toHexString(bs);
//					LOGGER.info("！！！！！！！！！！未知数据(2)v:" + data_ver + "t:" + data_type + ":" + resultStr);
//				}
            } else if (data_ver == 1) {
                if (data_type == 3) {
                    try {
//						PublicDataConf.ROOM_POPULARITY = ByteUtils.byteslong(bs);
                    } catch (Exception e) {
                        // TODO 自动生成的 catch 块
                        e.printStackTrace();
                    }
//					LOGGER.info(resultStr);
                }
//				else {
//					resultStr = HexUtils.toHexString(bs);
//					LOGGER.info("！！！！！！！！！！未知数据(2)v:" + data_ver + "t:" + data_type + ":" + resultStr);
//				}
            } else if (data_ver == 0) {
                try {
					resultStr = new String(bs, "utf-8");
                    MainConf.resultStrs.add(resultStr);
					if (MainConf.parseMessageThread != null && !MainConf.parseMessageThread.FLAG) {
						synchronized (MainConf.parseMessageThread) {
                            MainConf.parseMessageThread.notify();
						}
					}
                } catch (Exception e) {
                    // TODO 自动生成的 catch 块
                    e.printStackTrace();
                }
//				resultStr = ByteUtils.unicodeToString(resultStr);

            }
//			else {
//				resultStr = HexUtils.toHexString(bs);
//				LOGGER.info("！！！！！！！！！！未知数据(2):" + resultStr);
//			}
//			if (resultStr == null) {
//				resultStr=HexUtils.toHexString(bs);
//				LOGGER.info("空数据(2):"+resultStr);
//			}
//			index+=1;
            bs = null;
            offect += data_len;
        }
//		LOGGER.info(maxLen+"长的母数据包打印结束--------------------------------------------------------------------------------");
        resultStr = null;
    }


    // 两个方法
    // byte转换成实体类 解读弹幕
    // 实体类转换成byte 生成二进制数byte转发给服务器
    /**
     * 处理弹幕数据
     *
     * @param bytes 数据集
     * @return 1
     */
    public static BarrageHeadHandle unBEhandle(byte[] bytes) {
        BarrageHeadHandle barrageHeadHandle = BarrageHeadHandle.getBarrageHeadHandle();
        try {
            JavaStruct.unpack(barrageHeadHandle, bytes);
        } catch (StructException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }
        return barrageHeadHandle;
    }

    /**
     * 弹幕集打包
     *
     * @param barrageHeadHandle test
     * @return 新数组
     */
    public static byte[] BEhandle(BarrageHeadHandle barrageHeadHandle) {
        byte[] b=null;
        try {
            b= JavaStruct.pack(barrageHeadHandle);
        } catch (StructException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }
        return b;
    }
}
