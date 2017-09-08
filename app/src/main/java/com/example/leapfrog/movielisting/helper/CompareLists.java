package com.example.leapfrog.movielisting.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


public class CompareLists {


    public boolean equalLists(List<String> new_ones, List<String> old_ones) {
        if (new_ones == null && old_ones == null) {
            return true;
        }

        if ((new_ones == null && old_ones != null)
                || new_ones != null && old_ones == null
                || new_ones.size() != old_ones.size()) {
            return false;
        }

//to avoid messing the order of the lists we will use a copy
//as noted in comments by A. R. S.
        new_ones = new ArrayList<String>(new_ones);
        old_ones = new ArrayList<String>(old_ones);

        Collections.sort(new_ones);
        Collections.sort(old_ones);
        return new_ones.equals(old_ones);
    }

    public void filteroutListItems(ArrayList<String> new_list, ArrayList<String> old_list) {
        System.out.println("old list: " + old_list);
        System.out.println("old list: " + new_list);


        //removing stale elements from the old list
        boolean found;
        Iterator<String> it = old_list.iterator();
        while (it.hasNext()) {
            found = false;
            String oldid = it.next();

//            System.out.println("id to check: " + oldid);
            for (String newid : new_list) {

                if (oldid.equals(newid)) {
                    //means this old id matches with new id
                    //no need to further look on this oldid
                    //break the loop
                    found = true;
                    break;
                }
            }
            if (!found) {
                it.remove();
                //udpate adapter
            }
        }

        System.out.println("the filtered out list: " + old_list);

        //now adding new elements from the new list
        List<String> new_copy = new ArrayList<>(new_list);
        new_copy.removeAll(old_list);//remove same items
        System.out.println("the new ids are: " + new_copy);
        for (String newid : new_copy) {
            old_list.add(newid);
            //update adapter
        }
        System.out.println("the updated list: " + old_list);

    }
}
