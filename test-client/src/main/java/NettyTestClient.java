import com.liuhao.rpc.transport.RpcClient;
import com.liuhao.rpc.transport.RpcClientProxy;
import com.liuhao.rpc.api.HelloObject;
import com.liuhao.rpc.api.HelloService;
import com.liuhao.rpc.transport.netty.client.NettyClient;
import com.liuhao.rpc.serializer.ProtostuffSerializer;

public class NettyTestClient {
    public static void main(String[] args) {
        RpcClient client = new NettyClient();
        client.setSerializer(new ProtostuffSerializer());
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "this is netty test!!!");
        String res = helloService.hello(object);
        System.out.println(res);
    }
}
