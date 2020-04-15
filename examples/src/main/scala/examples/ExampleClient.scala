package examples

import io.grpc.{ManagedChannelBuilder, Metadata}
import examples.greeter._


object ExampleClient extends App {

  val channel = ManagedChannelBuilder.forAddress("192.168.0.3", 50051) .asInstanceOf[ManagedChannelBuilder[_ <: ManagedChannelBuilder[_]]]
    .usePlaintext().build

  val request = Request(name = "Hello")
  //Blocking call:
  val blockingStub = GreeterGrpc.blockingStub(channel)
  val reply:Response = blockingStub.greet(request)
  println(reply)


  //Blocking call:
  val blockingStubWithMetaData = GreeterGrpcWithMetadata.blockingStub(channel)

  val metadata = new Metadata()
  metadata.put(Metadata.Key.of("Name", Metadata.ASCII_STRING_MARSHALLER), "John")
  metadata.put(Metadata.Key.of("Age", Metadata.ASCII_STRING_MARSHALLER), "23")

  val reply2:Response = blockingStubWithMetaData.greet(request,metadata)
  println(reply2)

}
