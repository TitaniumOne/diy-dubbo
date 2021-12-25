import com.liuhao.rpc.serializer.CommonSerializer;
import com.liuhao.rpc.transport.RpcClient;
import com.liuhao.rpc.transport.RpcClientProxy;
import com.liuhao.rpc.api.HelloObject;
import com.liuhao.rpc.api.HelloService;
import com.liuhao.rpc.transport.netty.client.NettyClient;

public class NettyTestClient {
    public static void main(String[] args) {
        RpcClient client = new NettyClient(CommonSerializer.PROTOBUF_SERIALIZER);
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "this is netty test!!!");
        for(int i = 0; i < 20; i++) {
            String res = helloService.hello(object);
            System.out.println(res);
        }
    }
}
