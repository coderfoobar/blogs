
/**
 * 对Http头信息进行访问和操作
 */
public class HttpHeaderUtils {

    /**
     * get client ip address
     */
    public static String getClientIPAddr(HttpServletRequest request){
        boolean found = false;
        String ip =  = request.getHeader("x-forwarded-for");
        if (null != ip) {
            String[] ips = ip.split(",");
            for (String ipAddr : ips) {
                ip = ipAddr.trim();
                if (isIpv4Valid(ip) && !isIpv4Private(ip)) {
                    found = true;
                    break;
                }
            }
        }

        if (!found) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}