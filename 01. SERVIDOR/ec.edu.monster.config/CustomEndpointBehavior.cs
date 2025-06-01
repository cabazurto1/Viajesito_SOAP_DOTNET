using System;
using System.ServiceModel;
using System.ServiceModel.Channels;
using System.ServiceModel.Configuration;
using System.ServiceModel.Description;
using System.ServiceModel.Dispatcher;

namespace ec.edu.monster.config
{
    public class CustomEndpointBehavior : BehaviorExtensionElement, IEndpointBehavior
    {
        public override Type BehaviorType => typeof(CustomEndpointBehavior);

        protected override object CreateBehavior()
        {
            return new CustomEndpointBehavior();
        }

        public void ApplyDispatchBehavior(ServiceEndpoint endpoint, EndpointDispatcher endpointDispatcher)
        {
            endpointDispatcher.DispatchRuntime.MessageInspectors.Add(new CustomHeaderMessageInspector());
        }

        public void AddBindingParameters(ServiceEndpoint endpoint, BindingParameterCollection bindingParameters) { }

        public void ApplyClientBehavior(ServiceEndpoint endpoint, ClientRuntime clientRuntime) { }

        public void Validate(ServiceEndpoint endpoint) { }
    }
}
