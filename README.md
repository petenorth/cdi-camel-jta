# CDI with Narayana/JTA and transacted JMS endpoints.

This example shows how to work with Camel in the Java Container using CDI and the Narayana transaction manager. The JMS endpoint present in the route using the a JTA aware A-MQ component and the route it's self is set to be transacted. The route (org.pfry.cdijta.route.TomsRoutes) has a processor after the sending of a message to the transaction client JMS endpoint which throws a runtime exception on every fifth message. This means that of the 25 messages sent only 20 should end up on the A-MQ queue.

### Building

The example can be built with

    mvn clean install

### Running the example locally

Normally you would run locally using the following Maven goal but for this example this isn't possible:

    mvn clean install exec:java (WILL NOT WORK)


### Running the example in the CDK

It is assumed you have worked through

https://access.redhat.com/documentation/en/red-hat-enterprise-linux-atomic-host/7/container-development-kit-installation-guide/container-development-kit-installation-guide

https://access.redhat.com/documentation/en/red-hat-enterprise-linux-atomic-host/version-7/getting-started-with-container-development-kit/

My personal development environment (the host) is

1) CDK running on RHEL 7.2
2) VirtualBox 5.0.x
3) Vagrant 1.7.4

After 

vagrant up (registering the VM with my red hat support credentials)

then

eval "$(vagrant service-manager env docker)

then

oc login -u admin -p admin https://10.1.2.2:8443
oc new-project testing

I found that my CDK was missing Fuse Integration Image Streams and Templates. So I found I had to do

oc create -n openshift -f https://raw.githubusercontent.com/jboss-fuse/application-templates/master/fis-image-streams.json
oc create -f /home/pfry/projects/pd_20160508/openshift-ansible/roles/openshift_examples/files/examples/v1.1/xpaas-templates/ -n openshift

See

https://docs.openshift.com/enterprise/3.1/install_config/imagestreams_templates.html

Now from the Openshift web console select the testing project and create a broker app from the A-MQ 6.2 basic. This gives you a broker running in a pod exposing a number of services (for the different transport protocols). On the command line type

oc describe services broker-amq-tcp 

And make a note of the Cluster IP. Go into this project and edit the broker URL which is hard coded in ActiveMQXAConnectionFactoryProducer.java . I will be updating the code shortly so that this is obtained either from an environment property or via the Fabric8 @ServiceName annotation.

The example can now be built and deployed using a single goal:

    mvn -Pf8-deploy

When the example runs in OpenShift, you can use the OpenShift client tool to inspect the status

To list all the running pods:

    oc get pods

Then find the name of the pod that runs this quickstart, and output the logs from the running pods with:

    oc logs <name of pod>

You can also use the OpenShift [web console](https://docs.openshift.com/enterprise/3.1/getting_started/developers/developers_console.html#tutorial-video) to manage the
running pods, and view logs and much more.

In the logs you should see that every fifth messages fails to be processed by the route and then if you navigate to the Java console of the broker-amq pod (within the Openshift web console) the number of enqueued messages is 20 on the TEST.OUT queue.


### More details

The Main class used to boot CDI is a modified version of the Fuse Integration Services boot class 

org.pfry.cdijta.boot.Main

It is modified using code inspired by

https://github.com/jbosstm/quickstart/tree/master/jta-and-hibernate

You will see that there is some JPA/JTA code present in the code and a route

org.pfry.cdijta.MyRoutes 

Which uses it. The routes are disabled currently, feel free to switch the on and see how they work. I need to test the rollback behaviour with Postgres a similar database as currently the H2 database only works in AUTOCOMMIT mode.

You can find more details about running this [quickstart](http://fabric8.io/guide/quickstarts/running.html) on the website. This also includes instructions how to change the Docker image user and registry.

