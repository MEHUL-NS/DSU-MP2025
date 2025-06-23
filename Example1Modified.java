//package org.cloudbus.cloudsim.examples;
//
//import java.text.DecimalFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.LinkedList;
//import java.util.List;
//
//import org.cloudbus.cloudsim.*;
//import org.cloudbus.cloudsim.core.CloudSim;
//import org.cloudbus.cloudsim.provisioners.*;
//
//public class CloudSimExample1Modified {
//
//    private static List<Cloudlet> cloudletList;
//    private static List<Vm> vmlist;
//
//    public static void main(String[] args) {
//        Log.printLine("Starting CloudSimExample1 with RL-based VM Allocation...");
//
//        try {
//            // Step 1: Initialize CloudSim
//            int num_user = 1;
//            Calendar calendar = Calendar.getInstance();
//            boolean trace_flag = false;
//            CloudSim.init(num_user, calendar, trace_flag);
//
//            // Step 2: Create Datacenter with RL-based VM allocation
//            Datacenter datacenter0 = createDatacenter("Datacenter_0");
//
//            // Step 3: Create Broker
//            DatacenterBroker broker = createBroker();
//            int brokerId = broker.getId();
//
//            // Step 4: Create Virtual Machines (VMs)
//            vmlist = new ArrayList<>();
//            int vmid = 0;
//            int mips = 1000;
//            long size = 10000; 
//            int ram = 512;
//            long bw = 1000;
//            int pesNumber = 1; 
//            String vmm = "Xen"; 
//
//            Vm vm = new Vm(vmid, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
//            vmlist.add(vm);
//            broker.submitVmList(vmlist);
//
//            // Step 5: Create Cloudlets (Tasks)
//            cloudletList = new ArrayList<>();
//            int id = 0;
//            long length = 400000;
//            long fileSize = 300;
//            long outputSize = 300;
//            UtilizationModel utilizationModel = new UtilizationModelFull();
//
//            Cloudlet cloudlet = new Cloudlet(id, length, pesNumber, fileSize, outputSize, 
//                utilizationModel, utilizationModel, utilizationModel);
//            cloudlet.setUserId(brokerId);
//            cloudlet.setVmId(vmid);
//            cloudletList.add(cloudlet);
//
//            broker.submitCloudletList(cloudletList);
//
//            // Step 6: Start Simulation
//            CloudSim.startSimulation();
//            CloudSim.stopSimulation();
//
//            // Step 7: Print Results
//            List<Cloudlet> newList = broker.getCloudletReceivedList();
//            printCloudletList(newList);
//
//            Log.printLine("CloudSimExample1 with RL-based VM Allocation finished!");
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.printLine("Unexpected errors occurred.");
//        }
//    }
//
//    private static Datacenter createDatacenter(String name) {
//        List<Host> hostList = new ArrayList<>();
//        List<Pe> peList = new ArrayList<>();
//        int mips = 1000;
//        peList.add(new Pe(0, new PeProvisionerSimple(mips))); 
//
//        int hostId = 0;
//        int ram = 2048; 
//        long storage = 1000000;
//        int bw = 10000;
//
//        hostList.add(new Host(
//            hostId,
//            new RamProvisionerSimple(ram),
//            new BwProvisionerSimple(bw),
//            storage,
//            peList,
//            new VmSchedulerTimeShared(peList)
//        ));
//
//        String arch = "x86";
//        String os = "Linux";
//        String vmm = "Xen";
//        double time_zone = 10.0;
//        double cost = 3.0;
//        double costPerMem = 0.05;
//        double costPerStorage = 0.001;
//        double costPerBw = 0.0;
//        LinkedList<Storage> storageList = new LinkedList<>();
//
//        DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
//            arch, os, vmm, hostList, time_zone, cost, costPerMem, costPerStorage, costPerBw
//        );
//
//        Datacenter datacenter = null;
//        try {
//            // 🟢 Use the concrete RL-based policy implementation
//        	datacenter = new Datacenter(name, characteristics, new RLBasedVmAllocationPolicyImpl(hostList), storageList, 0);
//
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
//        }
//        return broker;
//    }
//
//    private static void printCloudletList(List<Cloudlet> list) {
//        int size = list.size();
//        String indent = "    ";
//        Log.printLine("\n========== OUTPUT ==========");
//        Log.printLine("Cloudlet ID" + indent + "STATUS" + indent + 
//                      "Data center ID" + indent + "VM ID" + indent + 
//                      "Time" + indent + "Start Time" + indent + "Finish Time");
//
//        DecimalFormat dft = new DecimalFormat("###.##");
//        for (Cloudlet cloudlet : list) {
//            Log.print(indent + cloudlet.getCloudletId() + indent + indent);
//
//            if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS) {
//                Log.print("SUCCESS");
//                Log.printLine(indent + indent + cloudlet.getResourceId() +
//                              indent + indent + indent + cloudlet.getVmId() +
//                              indent + indent + dft.format(cloudlet.getActualCPUTime()) +
//                              indent + indent + dft.format(cloudlet.getExecStartTime()) +
//                              indent + indent + dft.format(cloudlet.getFinishTime()));
//            }
//        }
//    }
//}




//
//package org.cloudbus.cloudsim.examples;
//
//import java.text.DecimalFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.LinkedList;
//import java.util.List;
//
//import org.cloudbus.cloudsim.*;
//import org.cloudbus.cloudsim.core.CloudSim;
//import org.cloudbus.cloudsim.provisioners.*;
//
//public class CloudSimExample1Modified {
//
//    private static List<Cloudlet> cloudletList;
//    private static List<Vm> vmlist;
//
//    public static void main(String[] args) {
//        Log.printLine("Starting CloudSimExample1 with RL-based VM Allocation...");
//
//        try {
//            // Step 1: Initialize CloudSim
//            int num_user = 1;
//            Calendar calendar = Calendar.getInstance();
//            boolean trace_flag = false;
//            CloudSim.init(num_user, calendar, trace_flag);
//
//            // Step 2: Create Datacenter with RL-based VM allocation
//            Datacenter datacenter0 = createDatacenter("Datacenter_0");
//
//            // Step 3: Create Broker
//            DatacenterBroker broker = createBroker();
//            int brokerId = broker.getId();
//
//            // Step 4: Create Multiple Virtual Machines (VMs)
//            vmlist = new ArrayList<>();
//            int numVMs = 10;
//            for (int i = 0; i < numVMs; i++) {
//                int mips = 1000;
//                long size = 10000; 
//                int ram = 512;
//                long bw = 1000;
//                int pesNumber = 1;
//                String vmm = "Xen"; 
//                Vm vm = new Vm(i, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
//                vmlist.add(vm);
//            }
//            broker.submitVmList(vmlist);
//
//            // Step 5: Create Multiple Cloudlets (Tasks)
//            cloudletList = new ArrayList<>();
//            int numCloudlets = 20;
//            for (int i = 0; i < numCloudlets; i++) {
//                long length = 400000;
//                long fileSize = 300;
//                long outputSize = 300;
//                UtilizationModel utilizationModel = new UtilizationModelFull();
//                Cloudlet cloudlet = new Cloudlet(i, length, 1, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
//                cloudlet.setUserId(brokerId);
//                cloudlet.setVmId(i % numVMs);
//                cloudletList.add(cloudlet);
//            }
//            broker.submitCloudletList(cloudletList);
//
//            // Step 6: Start Simulation
//            CloudSim.startSimulation();
//            CloudSim.stopSimulation();
//
//            // Step 7: Print Results
//            List<Cloudlet> newList = broker.getCloudletReceivedList();
//            printCloudletList(newList);
//
//            Log.printLine("CloudSimExample1 with RL-based VM Allocation finished!");
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.printLine("Unexpected errors occurred.");
//        }
//    }
//
//    private static Datacenter createDatacenter(String name) {
//        List<Host> hostList = new ArrayList<>();
//        List<Pe> peList = new ArrayList<>();
//        int mips = 1000;
//        peList.add(new Pe(0, new PeProvisionerSimple(mips))); 
//
//        int hostId = 0;
//        int ram = 2048;
//        long storage = 1000000;
//        int bw = 10000;
//
//        hostList.add(new Host(
//            hostId,
//            new RamProvisionerSimple(ram),
//            new BwProvisionerSimple(bw),
//            storage,
//            peList,
//            new VmSchedulerTimeShared(peList)
//        ));
//
//        String arch = "x86";
//        String os = "Linux";
//        String vmm = "Xen";
//        double time_zone = 10.0;
//        double cost = 3.0;
//        double costPerMem = 0.05;
//        double costPerStorage = 0.001;
//        double costPerBw = 0.0;
//        LinkedList<Storage> storageList = new LinkedList<>();
//
//        DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
//            arch, os, vmm, hostList, time_zone, cost, costPerMem, costPerStorage, costPerBw
//        );
//
//        Datacenter datacenter = null;
//        try {
//            datacenter = new Datacenter(name, characteristics, new RLBasedVmAllocationPolicyImpl(hostList), storageList, 0);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return datacenter;
//    }
//
//    private static DatacenterBroker createBroker() {
//        DatacenterBroker broker = null;
//        try {
//            broker = new DatacenterBroker("Broker");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return broker;
//    }
//
//    private static void printCloudletList(List<Cloudlet> list) {
//        int size = list.size();
//        String indent = "    ";
//        Log.printLine("\n========== OUTPUT ===========");
//        Log.printLine("Cloudlet ID" + indent + "STATUS" + indent + "Data center ID" + indent + "VM ID" + indent + "Time" + indent + "Start Time" + indent + "Finish Time");
//
//        DecimalFormat dft = new DecimalFormat("###.##");
//        for (Cloudlet cloudlet : list) {
//            Log.print(indent + cloudlet.getCloudletId() + indent + indent);
//            if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS) {
//                Log.print("SUCCESS");
//                Log.printLine(indent + indent + cloudlet.getResourceId() +
//                              indent + indent + indent + cloudlet.getVmId() +
//                              indent + indent + dft.format(cloudlet.getActualCPUTime()) +
//                              indent + indent + dft.format(cloudlet.getExecStartTime()) +
//                              indent + indent + dft.format(cloudlet.getFinishTime()));
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
import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.*;

public class CloudSimExample1Modified {

    private static List<Cloudlet> cloudletList;
    private static List<Vm> vmlist;

    public static void main(String[] args) {
        Log.printLine("Starting CloudSimExample1 with RL-based VM Allocation...");

        try {
            int num_user = 1;
            Calendar calendar = Calendar.getInstance();
            boolean trace_flag = false;
            CloudSim.init(num_user, calendar, trace_flag);

            // Step 2: Create Multiple Datacenters with More Hosts
            Datacenter datacenter0 = createDatacenter("Datacenter_0");
            Datacenter datacenter1 = createDatacenter("Datacenter_1");
            Datacenter datacenter2 = createDatacenter("Datacenter_2");

            // Step 3: Create Broker
            DatacenterBroker broker = createBroker();
            int brokerId = broker.getId();

            // Step 4: Create Multiple Virtual Machines (VMs)
            vmlist = new ArrayList<>();
            int numVMs = 100;  // Increased number of VMs
            for (int i = 0; i < numVMs; i++) {
//                int mips = 1000;
            	int mips = 1000 + (int) (Math.random() * 2500);  // Between 500 and 3000 MIPS
                long size = 10000;
                int ram = 512;
                long bw = 1000;
                int pesNumber = 1;
                String vmm = "Xen";
                Vm vm = new Vm(i, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
                vmlist.add(vm);
            }
            broker.submitVmList(vmlist);

            // Step 5: Create Multiple Cloudlets (Tasks)
            cloudletList = new ArrayList<>();
            int numCloudlets = 1000;  // Increased number of cloudlets
            for (int i = 0; i < numCloudlets; i++) {                
                long length = 100000 + (long) (Math.random() * 900000);  // Between 100K and 1M
                long fileSize = 300;
                long outputSize = 300;
                UtilizationModel utilizationModel = new UtilizationModelFull();
                Cloudlet cloudlet = new Cloudlet(i, length, 1, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
                cloudlet.setUserId(brokerId);
                cloudlet.setVmId(i % numVMs);
                cloudletList.add(cloudlet);
            }
            broker.submitCloudletList(cloudletList);

            // Step 6: Start Simulation
            CloudSim.startSimulation();
            CloudSim.stopSimulation();

            // Step 7: Print Results
            List<Cloudlet> newList = broker.getCloudletReceivedList();
            printCloudletList(newList);

            Log.printLine("CloudSimExample1 with RL-based VM Allocation finished!");
        } catch (Exception e) {
            e.printStackTrace();
            Log.printLine("Unexpected errors occurred.");
        }
    }

    private static Datacenter createDatacenter(String name) {
        List<Host> hostList = new ArrayList<>();
        
        // Increased number of hosts
        int numHosts = 10;  
        for (int i = 0; i < numHosts; i++) {
            List<Pe> peList = new ArrayList<>();
            int mips = 5000;  // Increased MIPS per host
            peList.add(new Pe(0, new PeProvisionerSimple(mips)));

            int ram = 8192;  // Increased RAM
            long storage = 1000000;
            int bw = 10000;

            hostList.add(new Host(i, new RamProvisionerSimple(ram), new BwProvisionerSimple(bw), storage, peList, new VmSchedulerTimeShared(peList)));
        }

        String arch = "x86";
        String os = "Linux";
        String vmm = "Xen";
        double time_zone = 10.0;
        double cost = 3.0;
        double costPerMem = 0.05;
        double costPerStorage = 0.001;
        double costPerBw = 0.0;
        LinkedList<Storage> storageList = new LinkedList<>();

        DatacenterCharacteristics characteristics = new DatacenterCharacteristics(arch, os, vmm, hostList, time_zone, cost, costPerMem, costPerStorage, costPerBw);

        Datacenter datacenter = null;
        try {
            datacenter = new Datacenter(name, characteristics, new RLBasedVmAllocationPolicyImpl(hostList), storageList, 0);
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
        }
        return broker;
    }

//    private static void printCloudletList(List<Cloudlet> list) {
//        int size = list.size();
//        String indent = "    ";
//        Log.printLine("\n========== OUTPUT ===========");
//        Log.printLine("Cloudlet ID" + indent + "STATUS" + indent + "Data center ID" + indent + "VM ID" + indent + "Time" + indent + "Start Time" + indent + "Finish Time");
//
//        DecimalFormat dft = new DecimalFormat("###.##");
//        for (Cloudlet cloudlet : list) {
//            Log.print(indent + cloudlet.getCloudletId() + indent + indent);
//            if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS) {
//                Log.print("SUCCESS");
//                Log.printLine(indent + indent + cloudlet.getResourceId() +
//                              indent + indent + indent + cloudlet.getVmId() +
//                              indent + indent + dft.format(cloudlet.getActualCPUTime()) +
//                              indent + indent + dft.format(cloudlet.getExecStartTime()) +
//                              indent + indent + dft.format(cloudlet.getFinishTime()));
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
        
        double powerPerVM = 5.0;  // Assume each VM uses 50 Watts
        double costPerSec = 1.5;   // Cost per second from Datacenter config

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


