//package org.cloudbus.cloudsim.examples;
//
//import java.text.DecimalFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.LinkedList;
//import java.util.List;
//
//import org.cloudbus.cloudsim.Cloudlet;
//import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
//import org.cloudbus.cloudsim.Datacenter;
//import org.cloudbus.cloudsim.DatacenterBroker;
//import org.cloudbus.cloudsim.DatacenterCharacteristics;
//import org.cloudbus.cloudsim.Host;
//import org.cloudbus.cloudsim.Log;
//import org.cloudbus.cloudsim.Pe;
//import org.cloudbus.cloudsim.Storage;
//import org.cloudbus.cloudsim.UtilizationModel;
//import org.cloudbus.cloudsim.UtilizationModelFull;
//import org.cloudbus.cloudsim.Vm;
//import org.cloudbus.cloudsim.VmAllocationPolicySimple;
//import org.cloudbus.cloudsim.VmSchedulerTimeShared;
//import org.cloudbus.cloudsim.core.CloudSim;
//import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
//import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
//import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;
//
//public class CloudSimExample {
//
//    private static List<Cloudlet> cloudletList;
//    private static List<Vm> vmlist;
//
//    public static void main(String[] args) {
//
//        Log.printLine("Starting CloudSimExample1...");
//
//        try {
//            int num_user = 1; // Number of cloud users
//            Calendar calendar = Calendar.getInstance();
//            boolean trace_flag = false; // Set to true for event tracing
//
//            // Initialize the CloudSim library
//            CloudSim.init(num_user, calendar, trace_flag);
//
//            // Create multiple datacenters (e.g., 3 datacenters to handle 10 VMs)
//            Datacenter datacenter0 = createDatacenter("Datacenter_0");
//            Datacenter datacenter1 = createDatacenter("Datacenter_1");
//            Datacenter datacenter2 = createDatacenter("Datacenter_2");
//
//            // Create Broker
//            DatacenterBroker broker = createBroker();
//            int brokerId = broker.getId();
//
//            // Create multiple VMs (e.g., 10 VMs)
//            vmlist = new ArrayList<Vm>();
//
//            for (int i = 0; i < 10; i++) {
//                int vmid = i;
//                int mips = 1000;
//                long size = 10000; // image size (MB)
//                int ram = 512; // VM memory (MB)
//                long bw = 1000;
//                int pesNumber = 1; // number of CPUs
//                String vmm = "Xen"; // Virtual Machine Monitor name
//
//                Vm vm = new Vm(vmid, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
//                vmlist.add(vm);
//            }
//
//            // Submit VM list to the broker
//            broker.submitVmList(vmlist);
//
//            // Create Cloudlets (10 Cloudlets, one per VM)
//            cloudletList = new ArrayList<Cloudlet>();
//            for (int i = 0; i < 10; i++) {
//                int id = i;
//                long length = 400000;
//                long fileSize = 300;
//                long outputSize = 300;
//                UtilizationModel utilizationModel = new UtilizationModelFull();
//
//                Cloudlet cloudlet = new Cloudlet(id, length, 1, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
//                cloudlet.setUserId(brokerId);
//                cloudlet.setVmId(i); // Cloudlet associated with the VM by ID
//                cloudletList.add(cloudlet);
//            }
//
//            // Submit Cloudlet list to the broker
//            broker.submitCloudletList(cloudletList);
//
//            // Start the simulation
//            CloudSim.startSimulation();
//            CloudSim.stopSimulation();
//
//            // Print results when simulation is over
//            List<Cloudlet> newList = broker.getCloudletReceivedList();
//            printCloudletList(newList);
//
//            Log.printLine("CloudSimExample1 finished!");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.printLine("Unwanted errors happen");
//        }
//    }
//
//    private static Datacenter createDatacenter(String name) {
//        List<Host> hostList = new ArrayList<Host>();
//
//        // Each Datacenter will have a few hosts (e.g., 2 hosts)
//        for (int i = 0; i < 2; i++) {
//            List<Pe> peList = new ArrayList<Pe>();
//            int mips = 2000;
//
//            peList.add(new Pe(0, new PeProvisionerSimple(mips))); // Add one PE
//
//            int hostId = i;
//            int ram = 2048; // Host memory (MB)
//            long storage = 1000000; // Host storage (MB)
//            int bw = 10000; // Host bandwidth
//
//            // Create the host
//            hostList.add(new Host(hostId, new RamProvisionerSimple(ram), new BwProvisionerSimple(bw), storage, peList, new VmSchedulerTimeShared(peList)));
//        }
//
//        String arch = "x86"; // System architecture
//        String os = "Linux"; // Operating system
//        String vmm = "Xen"; // Virtual Machine Monitor
//        double time_zone = 10.0; // Time zone this resource located
//        double cost = 3.0; // Cost of using processing in this resource
//        double costPerMem = 0.05; // Cost of using memory in this resource
//        double costPerStorage = 0.001; // Cost of using storage in this resource
//        double costPerBw = 0.0; // Cost of using bandwidth in this resource
//        LinkedList<Storage> storageList = new LinkedList<Storage>(); // No SAN devices
//
//        DatacenterCharacteristics characteristics = new DatacenterCharacteristics(arch, os, vmm, hostList, time_zone, cost, costPerMem, costPerStorage, costPerBw);
//
//        Datacenter datacenter = null;
//        try {
//            datacenter = new Datacenter(name, characteristics, new VmAllocationPolicySimple(hostList), storageList, 0);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return datacenter;
//    }
//
//    private static DatacenterBroker createBroker() {
//        DatacenterBroker broker = null;
//        try {
//            broker = new DatacenterBroker("Broker");
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//        return broker;
//    }
//
//    private static void printCloudletList(List<Cloudlet> list) {
//        int size = list.size();
//        Cloudlet cloudlet;
//        String indent = "    ";
//
//        Log.printLine();
//        Log.printLine("========== OUTPUT ==========");
//        Log.printLine("Cloudlet ID" + indent + "STATUS" + indent + "Data center ID" + indent + "VM ID" + indent + "Time" + indent + "Start Time" + indent + "Finish Time");
//
//        DecimalFormat dft = new DecimalFormat("###.##");
//        for (int i = 0; i < size; i++) {
//            cloudlet = list.get(i);
//            Log.print(indent + cloudlet.getCloudletId() + indent + indent);
//
//            if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS) {
//                Log.print("SUCCESS");
//                Log.printLine(indent + indent + cloudlet.getResourceId() + indent + indent + cloudlet.getVmId() + indent + dft.format(cloudlet.getActualCPUTime()) + indent + dft.format(cloudlet.getExecStartTime()) + indent + dft.format(cloudlet.getFinishTime()));
//            }
//        }
//    }
//}





package org.cloudbus.cloudsim.examples;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

public class CloudSimExample {

    private static List<Cloudlet> cloudletList;
    private static List<Vm> vmlist;

    public static void main(String[] args) {

        Log.printLine("Starting CloudSimExample...");

        try {
            int num_user = 1; // Number of cloud users
            Calendar calendar = Calendar.getInstance();
            boolean trace_flag = false; // Set to true for event tracing

            // Initialize the CloudSim library
            CloudSim.init(num_user, calendar, trace_flag);

            // Create Datacenters
            Datacenter datacenter0 = createDatacenter("Datacenter_0");
            Datacenter datacenter1 = createDatacenter("Datacenter_1");
            System.out.println("Datacenters created: " + datacenter0.getName() + ", " + datacenter1.getName());

            // Create Broker
            DatacenterBroker broker = createBroker();
            int brokerId = broker.getId();

            // Create 100 VMs
            vmlist = new ArrayList<Vm>();
            for (int i = 0; i < 100; i++) {
                int vmid = i;
                int mips = 1000;
                long size = 10000; // image size (MB)
                int ram = 512; // VM memory (MB)
                long bw = 1000;
                int pesNumber = 1; // number of CPUs
                String vmm = "Xen"; // Virtual Machine Monitor name

                Vm vm = new Vm(vmid, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
                vmlist.add(vm);
            }

            // Submit VM list to the broker
            broker.submitVmList(vmlist);

            // Create 1000 Cloudlets
            cloudletList = new ArrayList<Cloudlet>();
            for (int i = 0; i < 1000; i++) {
                int id = i;

                // Randomly vary the cloudlet length to simulate different workloads
                long length = (long) (Math.random() * 1000000);  // Random length between 0 and 1 million
                long fileSize = 300;
                long outputSize = 300;
                UtilizationModel utilizationModel = new UtilizationModelFull();

                // Create cloudlet
                Cloudlet cloudlet = new Cloudlet(id, length, 1, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
                cloudlet.setUserId(brokerId);

                // Stagger the start times
                cloudlet.setExecStartTime(i * 0.1);  // Delay each cloudlet's start by 0.1 seconds

                cloudletList.add(cloudlet);
            }

            // Submit Cloudlet list to the broker
            broker.submitCloudletList(cloudletList);

            // Start the simulation
            CloudSim.startSimulation();
            CloudSim.stopSimulation();

            // Print results when simulation is over
            List<Cloudlet> newList = broker.getCloudletReceivedList();
            printCloudletList(newList);

            Log.printLine("CloudSimExample finished!");

        } catch (Exception e) {
            e.printStackTrace();
            Log.printLine("Unwanted errors happen");
        }
    }

    private static Datacenter createDatacenter(String name) {
        List<Host> hostList = new ArrayList<Host>();

        // Each Datacenter will have a few hosts (e.g., 2 hosts)
        for (int i = 0; i < 10; i++) {
            List<Pe> peList = new ArrayList<Pe>();
            int mips = 2000;

            peList.add(new Pe(0, new PeProvisionerSimple(mips))); // Add one PE

            int hostId = i;
            int ram = 2048; // Host memory (MB)
            long storage = 1000000; // Host storage (MB)
            int bw = 10000; // Host bandwidth

            // Create the host
            hostList.add(new Host(hostId, new RamProvisionerSimple(ram), new BwProvisionerSimple(bw), storage, peList, new VmSchedulerTimeShared(peList)));
        }

        String arch = "x86"; // System architecture
        String os = "Linux"; // Operating system
        String vmm = "Xen"; // Virtual Machine Monitor
        double time_zone = 10.0; // Time zone this resource located
        double cost = 3.0; // Cost of using processing in this resource
        double costPerMem = 0.05; // Cost of using memory in this resource
        double costPerStorage = 0.001; // Cost of using storage in this resource
        double costPerBw = 0.0; // Cost of using bandwidth in this resource
        LinkedList<Storage> storageList = new LinkedList<Storage>(); // No SAN devices

        DatacenterCharacteristics characteristics = new DatacenterCharacteristics(arch, os, vmm, hostList, time_zone, cost, costPerMem, costPerStorage, costPerBw);

        Datacenter datacenter = null;
        try {
            datacenter = new Datacenter(name, characteristics, new VmAllocationPolicySimple(hostList), storageList, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return datacenter;
    }

    private static DatacenterBroker createBroker() {
        DatacenterBroker broker = null;
        try {
            broker = new DatacenterBroker("Broker");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return broker;
    }

//    private static void printCloudletList(List<Cloudlet> list) {
//        int size = list.size();
//        Cloudlet cloudlet;
//        String indent = "    ";
//
//        Log.printLine();
//        Log.printLine("========== OUTPUT ==========");
//        Log.printLine("Cloudlet ID" + indent + "STATUS" + indent + "Data center ID" + indent + "VM ID" + indent + "Time" + indent + "Start Time" + indent + "Finish Time");
//
//        DecimalFormat dft = new DecimalFormat("###.##");
//        for (int i = 0; i < size; i++) {
//            cloudlet = list.get(i);
//            Log.print(indent + cloudlet.getCloudletId() + indent + indent);
//
//            if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS) {
//                Log.print("SUCCESS");
//                Log.printLine(indent + indent + cloudlet.getResourceId() + indent + indent + cloudlet.getVmId() + indent + dft.format(cloudlet.getActualCPUTime()) + indent + dft.format(cloudlet.getExecStartTime()) + indent + dft.format(cloudlet.getFinishTime()));
//            }
//        }
//    }
    
    private static void printCloudletList(List<Cloudlet> list) {
        int size = list.size();
        Cloudlet cloudlet;
        String indent = "    ";

        Log.printLine();
        Log.printLine("========== OUTPUT ==========");
        Log.printLine("Cloudlet ID" + indent + "STATUS" + indent + "Data center ID" + indent + 
                      "VM ID" + indent + "Time" + indent + "Start Time" + indent + 
                      "Finish Time" + indent + "Energy (J)" + indent + "Cost");

        DecimalFormat dft = new DecimalFormat("###.##");
        
        double powerPerVM = 5.0;  // Assume each VM uses 5 Watts
        double costPerSec = 1.5;   // Cost per second

        for (int i = 0; i < size; i++) {
            cloudlet = list.get(i);
            Log.print(indent + cloudlet.getCloudletId() + indent + indent);

            if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS) {
                double executionTime = cloudlet.getActualCPUTime();
                double energyConsumed = powerPerVM * executionTime;  // Energy = Power * Time
                double cost = executionTime * costPerSec;            // Cost = Time * Cost per sec

                Log.print("SUCCESS");
                Log.printLine(indent + indent + cloudlet.getResourceId() + indent + indent + 
                              cloudlet.getVmId() + indent + dft.format(executionTime) + indent + 
                              dft.format(cloudlet.getExecStartTime()) + indent + 
                              dft.format(cloudlet.getFinishTime()) + indent + 
                              dft.format(energyConsumed) + indent + 
                              dft.format(cost));
            }
        }
    }

    

}
