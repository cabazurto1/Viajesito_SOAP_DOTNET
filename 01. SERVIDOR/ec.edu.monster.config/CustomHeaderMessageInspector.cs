using System.ServiceModel;
using System.ServiceModel.Channels;
using System.ServiceModel.Dispatcher;

namespace ec.edu.monster.config
{
    public class CustomHeaderMessageInspector : IDispatchMessageInspector
    {
        public object AfterReceiveRequest(ref Message request, IClientChannel channel, InstanceContext instanceContext)
        {
            // No hacemos nada al recibir la petición
            return null;
        }

        public void BeforeSendReply(ref Message reply, object correlationState)
        {
            if (reply.Properties.ContainsKey(HttpResponseMessageProperty.Name))
            {
                var httpHeader = (HttpResponseMessageProperty)reply.Properties[HttpResponseMessageProperty.Name];

                // Asegura que los headers CORS se agregan correctamente
                httpHeader.Headers["Access-Control-Allow-Origin"] = "*";
                httpHeader.Headers["Access-Control-Allow-Methods"] = "GET, POST, OPTIONS";
                httpHeader.Headers["Access-Control-Allow-Headers"] = "Content-Type, SOAPAction";
            }
            else
            {
                var httpHeader = new HttpResponseMessageProperty();
                httpHeader.Headers["Access-Control-Allow-Origin"] = "*";
                httpHeader.Headers["Access-Control-Allow-Methods"] = "GET, POST, OPTIONS";
                httpHeader.Headers["Access-Control-Allow-Headers"] = "Content-Type, SOAPAction";
                reply.Properties.Add(HttpResponseMessageProperty.Name, httpHeader);
            }
        }
    }
}
