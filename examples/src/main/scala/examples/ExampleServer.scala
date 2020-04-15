package examples



import java.net.InetAddress
import java.util.logging.Logger
import examples.greeter._
import io.grpc.stub.StreamObserver
import io.grpc.{Server, ServerBuilder}
import scala.concurrent.{ExecutionContext, Future}


object ExampleServer {
  private val logger = Logger.getLogger(classOf[ExampleServer].getName)

  def main(args: Array[String]): Unit = {
    val server = new ExampleServer(ExecutionContext.global)
    server.start()
    server.blockUntilShutdown()
  }

  private val port = 50051
}

class ExampleServer(executionContext: ExecutionContext) { self =>
  private[this] var server: Server = null

  private def start(): Unit = {
    server = ServerBuilder.forPort(ExampleServer.port).asInstanceOf[ServerBuilder[_ <: ServerBuilder[_]]]
      .addService(GreeterGrpc.bindService(new GreeterImpl, executionContext))
      .build.start
    ExampleServer.logger.info("Server started, listening on " + ExampleServer.port + s", Server: ${InetAddress.getLocalHost}")
    Runtime.getRuntime.addShutdownHook(new Thread() {
      override def run(): Unit = {
        System.err.println("*** shutting down gRPC server since JVM is shutting down")
        self.stop()
        System.err.println("*** server shut down")
      }
    })
  }

  private def stop(): Unit = {
    if (server != null) {
      server.shutdown()
    }
  }

  private def blockUntilShutdown(): Unit = {
    if (server != null) {
      server.awaitTermination()
    }
  }

  private class GreeterImpl extends GreeterGrpc.Greeter {
    override def greet(request: Request): Future[Response] = {
        val reply = Response(request.name + " Client")
        ExampleServer.logger.info(s"Receive Req: `${request.toString}`, from server: ${InetAddress.getLocalHost.getHostAddress}")
        Future.successful(reply)
  }

    override def points(request: Request, responseObserver: StreamObserver[Point]): Unit = ???

    override def bidi(responseObserver: StreamObserver[Response]): StreamObserver[Point] = ???
  }

}

