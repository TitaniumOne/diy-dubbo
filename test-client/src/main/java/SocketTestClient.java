import com.liuhao.rpc.transport.RpcClient;
import com.liuhao.rpc.api.HelloObject;
import com.liuhao.rpc.api.HelloService;
import com.liuhao.rpc.transport.RpcClientProxy;
import com.liuhao.rpc.serializer.KryoSerializer;
import com.liuhao.rpc.transport.socket.client.SocketClient;

public class SocketTestClient {
    public static void main(String[] args) {
        RpcClient client = new SocketClient();
        client.setSerializer(new KryoSerializer());
        // 接口与代理对象之间的中介对象
        RpcClientProxy proxy = new RpcClientProxy(client);
        // 创建代理对象
        HelloService helloServiceProxy = proxy.getProxy(HelloService.class);
        // 接口方法的参数对象
        HelloObject helloObject = new HelloObject(12, "test message!!!!");
        // 由代理对象执行方法
        String res = helloServiceProxy.hello(helloObject);
        System.out.println(res);
    }
}
