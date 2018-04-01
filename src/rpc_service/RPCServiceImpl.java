package rpc_service;

public class RPCServiceImpl implements RPCService {
    @Override
    public String getInformation(String s) {
        return ("收到发来的信息：" + s);
    }

}
