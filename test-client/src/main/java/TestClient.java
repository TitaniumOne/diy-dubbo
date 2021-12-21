import com.liuhao.rpc.api.HelloObject;
import com.liuhao.rpc.api.HelloService;
import com.liuhao.rpc.client.RpcClientProxy;

public class TestClient {
    public static void main(String[] args) {
        // 接口与代理对象之间的中介对象
        RpcClientProxy proxy = new RpcClientProxy("127.0.0.1", 9000);
        // 创建代理对象
        HelloService helloServiceProxy = proxy.getProxy(HelloService.class);
        // 接口方法的参数对象
        HelloObject helloObject = new HelloObject(12, "test message!!!!");
        // 由代理对象执行方法
        String res = helloServiceProxy.hello(helloObject);
        System.out.println(res);
    }
}
