import com.liuhao.rpc.RpcClient;
import com.liuhao.rpc.RpcClientProxy;
import com.liuhao.rpc.api.HelloObject;
import com.liuhao.rpc.api.HelloService;
import com.liuhao.rpc.netty.client.NettyClient;
import com.liuhao.rpc.serializer.HessianSerializer;
import com.liuhao.rpc.serializer.KryoSerializer;

public class NettyTestClient {
    public static void main(String[] args) {
        RpcClient client = new NettyClient("127.0.0.1", 9999);
        client.setSerializer(new KryoSerializer());
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "this is netty test!!!");
        String res = helloService.hello(object);
        System.out.println(res);
    }
}
