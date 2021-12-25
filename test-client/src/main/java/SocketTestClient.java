import com.liuhao.rpc.loadbalancer.RoundRobinLoadBalancer;
import com.liuhao.rpc.serializer.CommonSerializer;
import com.liuhao.rpc.transport.RpcClient;
import com.liuhao.rpc.api.HelloObject;
import com.liuhao.rpc.api.HelloService;
import com.liuhao.rpc.transport.RpcClientProxy;
import com.liuhao.rpc.serializer.KryoSerializer;
import com.liuhao.rpc.transport.socket.client.SocketClient;

public class SocketTestClient {
    public static void main(String[] args) {
        RpcClient client = new SocketClient(CommonSerializer.PROTOBUF_SERIALIZER, new RoundRobinLoadBalancer());
        // 接口与代理对象之间的中介对象
        RpcClientProxy proxy = new RpcClientProxy(client);
        // 创建代理对象
        HelloService helloServiceProxy = proxy.getProxy(HelloService.class);
        // 接口方法的参数对象
        HelloObject helloObject = new HelloObject(12, "this is socket message!!!!");
        for(int i = 0; i < 20; i++) {
            // 由代理对象执行方法
            String res = helloServiceProxy.hello(helloObject);
            System.out.println(res);
        }
    }
}
