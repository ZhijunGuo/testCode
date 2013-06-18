package com.example.galaxy08.http;

/**
 * 
 * @author qingfeng.liu
 *
 */
public class ResponseCode {

    // 操作成功
    public static final String SUCCESS = "0";

    public static final String SYSTEM_ERROR = "1";
                       
    public static final String ERROR_PARAMS = "2";
                        
    public static final String INVALID_VERSION = "3";
                       
    public static final String ILLEGAL_REQUEST = "4";
                        
    public static final String NOT_WHITE_USER = "10";
                       
    public static final String SYSTEM_ERROR2 = "100";
                        
    public static final String NO_SUCH_ACCOUNT = "101";
                        
    // 账户状态被锁定或者限制     
    public static final String ACCOUNT_STATUS_ERROR = "102";
                        
    // 密码不正确             
    public static final String PASSWORD_UNCORRECT = "103";
                       
    // 注册账号已存在         
    public static final String ACCOUNT_EXISTED = "104";
                        
    public static final String PASSWORDS_NOT_MATCH = "105";
                        
    // 错误的验证码            
    public static final String ERROR_VERIFY_CODE = "106";
                        
    // 没有登录              
    public static final String NEED_LOGIN = "107";
                        
    // 名片不存在            String
    public static final String NO_SUCH_CARD = "110";
                        
    public static final String NOT_OWN_CARD = "111";
                        
    public static final String INCOMPLETE_CARD_INFO = "112";
                        
    public static final String TARGET_INFO_INCOMPLETE = "113";
                        
    public static final String CLAIMED_CARD_NOT_EXISTED = "114";
                        
    // 消息已被删除          
    public static final String MESSAGE_IS_DELETE = "120";
                       
    public static final String TWICE_REQUEST = "121";
    // 已是联系人         
    public static final String CONTACTED_NO_REQUEST_AGAIN = "122";
    // 文件错误           
    public static final String FILE_UPLOAD_ERROR = "131";
    //超出上传识别配额
    public static final String OVER_QUOTA = "202";
    
    // 分组名为空          
    public static final String EMPTY_GROUP_NAME = "220";
    
    // 用户尚未激活或者被封禁      
    public static final String ERROR_PRIVATE_SETTING = "221";
    // 301=目标用户不能是自己    
    public static final String TARGET_CANNOT_BE_YOUSELF = "301";
    //不是第三方用户
    public static final String CODE_NOT_THIRD_USER ="412";

    /**
     * #1-100为系统级错误 1=系统错误 2=调用参数错误 3=版本错误 4=没有调用权限 #一般是测试系统出现 10=不是白名单用户
     * 
     * 100=系统错误 ### #100以上为方法级错误 ### 101=账户不存在 102=账户状态错误 103=密码错误 104=注册账号已存在
     * 105=两次密码输入不一致 106=错误的验证码 107=账号没有登录
     * 
     * 110=名片不存在 111=您没有这张名片 112=名片信息不全 113=对方名片信息不全,无法加为联系人 114=认领的名片不存在
     * 
     * 120=消息已删除 121=已发送请求 122=已成为联系人无需发送请求
     * 
     * 131=文件上传错误
     * 
     * 220=分组名为空 221=错误的隐私设置
     * 
     * 301=目标用户不能是自己
     */

}
