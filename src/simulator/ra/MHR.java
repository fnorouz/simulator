/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simulator.ra;

import java.util.ArrayList;
import simulator.physical.BladeServer;
import simulator.EnterpriseSystem;
import simulator.InteractiveSystem;

/**
 *
 * @author fnorouz
 */
public class MHR extends ResourceAllocation {

    int[] powIndex = {15, 31, 16, 11, 36, 10, 30, 6, 20, 21, 35, 32, 17, 26, 25, 7, 27, 12, 42, 37, 41, 5, 2, 1, 0, 22, 40, 47, 46, 13, 45, 29, 23, 8, 28, 43, 48, 9, 38, 33, 18, 3, 34, 44, 24, 14, 49, 19, 39, 4};

    @Override
    public int nextServer(ArrayList<BladeServer> bs) {
        int i = 0, j = 0;
        for (i = 0; i < powIndex.length; i++) {
            for (j = 0; j < bs.size(); j++) {
                if (bs.get(j).ready == 1 && powIndex[i] == bs.get(j).chassisID) {
                    return j;
                }
            }

        }
        return -2;
    }

    @Override
    public int nextServerInSys(ArrayList<BladeServer> bs) {
        int i = 0, j = 0;
        for (i = 0; i < powIndex.length; i++) {
            for (j = 0; j < bs.size(); j++) {
                if (bs.get(j).ready == -2 && powIndex[i] == bs.get(j).chassisID) {
                    return j;
                }
            }

        }
        return -2;
    }

    @Override
    public int[] nextServerSys(ArrayList<Integer> chassisList) {
        int[] retValue = new int[2];
        retValue[0] = -2;
        retValue[1] = -2;

        ////////////////////////
        for (int j = powIndex.length - 1; j >= 0; j--) {
            int l = 0;
            for (l = 0; l < chassisList.size(); l++) {
                if (powIndex[j] == chassisList.get(l)) {
                    break;
                }
            }
            if (l != chassisList.size()) //in found chassis looking for a ready server
            {
                for (int k = 0; k < dc.chassisSet.get(chassisList.get(l)).servers.size(); k++) {
                    if (dc.chassisSet.get(chassisList.get(l)).servers.get(k).ready == -3) {
                        retValue[0] = chassisList.get(l);  // chassis id
                        retValue[1] = k;  //Server ID
                        return retValue;
                    }
                }
            }
        }
        return retValue;
    }
    //this funtion is used in ComputeSystem for allocating resources
    //List is array of compute nodes

    @Override
    public int[] allocateSystemLevelServer(ArrayList<BladeServer> ComputeNodeList, int list[]) {
        int j = 0, i = 0;
        int totalReadyNodes = 0;
        for (i = 0; i < list.length; i++) {
            list[i] = -2;
        }
        for (int k = 0; k < ComputeNodeList.size(); k++) {
            if (ComputeNodeList.get(k).ready == 1) {
                totalReadyNodes++;
            }
        }
        if (totalReadyNodes < list.length) {
            return list; //there is not enought ready node to accept this job
        }        //in CS which compute node is ready just save its index
        i = 0;
        j = 0;
        int k = powIndex.length - 1;
        for (; k >= 0 && j < list.length; k--) {
            for (i = 0; i < ComputeNodeList.size(); i++) {
                if (ComputeNodeList.get(i).ready == 1 && powIndex[k] == ComputeNodeList.get(i).chassisID)//& ComputeNodeList.get(i).activeBatchList.size()==0)
                {
                    list[j++] = i;
                    if (j == list.length) {
                        break;
                    }
                }
            }
        }
        //}

        return list;
    }

    @Override
    public void resourceAloc(InteractiveSystem WS) {
        // ToDo
    }

    @Override
    public void resourceAloc(EnterpriseSystem ES) {
    }
}
