package folders.comperator;

import java.io.*;
import java.util.*;

public class ComparingFiles {
        private BufferedReader brVersion1 = null;
        private BufferedReader brVersion2 = null;
        private String destinationPath;
        private String oldFolderNameVersion;
        private String newFolderNameVersion;
        private boolean isNewVersion1;
        private final int padding = 30;

    public ComparingFiles(String version1, String version2 , String destinationPath, String oldPath , String newPath) throws IOException {

        this.brVersion1 = new BufferedReader(new FileReader(version1));
        this.brVersion2 = new BufferedReader(new FileReader(version2));
        this.destinationPath=destinationPath;
        if(version1.contains(oldPath)){
            this.oldFolderNameVersion=oldPath.substring(oldPath.lastIndexOf("\\")+1,oldPath.length());
            this.newFolderNameVersion=newPath.substring(newPath.lastIndexOf("\\")+1,newPath.length());
            isNewVersion1 = false;
        }
        else {
            isNewVersion1 = true;
            this.newFolderNameVersion=newPath.substring(oldPath.lastIndexOf("\\")+1,oldPath.length());
            this.oldFolderNameVersion=oldPath.substring(newPath.lastIndexOf("\\")+1,newPath.length());
        }
        printDiffToFile();
    }

    private void printDiffToFile() throws IOException {
         Map<Integer, String> mapVersion1 = new HashMap<>();
         Map<Integer, String> mapVersion2 = new HashMap<>();
         int counter = 0;
         String sCurrentLine;
        PrintWriter writer = new PrintWriter(new FileOutputStream(
                new File(this.destinationPath),
                false /* append = true */));

         while ((sCurrentLine = brVersion1.readLine()) != null) {
            counter++;
            mapVersion1.put(counter, sCurrentLine);
        }

        counter = 0;
        while ((sCurrentLine = brVersion2.readLine()) != null) {
            counter++;
            mapVersion2.put(counter, sCurrentLine);
        }

        List<String> tmpList = new ArrayList<String>(mapVersion1.values());
        tmpList.removeAll(mapVersion2.values());
        Map<Integer , String> tmpMap = mapVersion1;
        if(isNewVersion1){
            writer.println("---------content from "+ oldFolderNameVersion +"(old version) which is not there in "+newFolderNameVersion + "(new version)-------------");
            writer.println();
        }
        else {
            writer.println("---------content from " + newFolderNameVersion + "(new version) which is not there in " + oldFolderNameVersion + "(old version)-------------");
            writer.println();
        }
        for (int i = 0; i < tmpList.size(); i++) {
            Set<Integer> tmpSet = getKeysByValue(tmpMap ,tmpList.get(i));
            if (tmpSet.size()==0){
                continue;
            }
            writer.println(padAgain("Lines " +tmpSet+ ": ")+ tmpList.get(i));
            if(tmpSet.size()>1){
                for (Integer key: tmpSet) {
                    tmpMap.remove(key);
                }
            }
        }
        if(!isNewVersion1){
            writer.println();
            writer.println("---------content from "+ oldFolderNameVersion +"(old version) which is not there in "+newFolderNameVersion + "(new version)-------------");
            writer.println();
        }
        else
        {
            writer.println();
            writer.println("---------content from " + newFolderNameVersion + "(new version) which is not there in " + oldFolderNameVersion + "(old version)-------------");
            writer.println();
        }
        tmpList = new ArrayList<>(mapVersion2.values());
        tmpList.removeAll(mapVersion1.values());
        for (int i = 0; i < tmpList.size(); i++) {
            Set<Integer> tmpSet = getKeysByValue(mapVersion2 ,tmpList.get(i));
            if (tmpSet.size()==0){
                continue;
            }
            writer.println(padAgain("Lines " + getKeysByValue(mapVersion2 ,tmpList.get(i)) + ": ")+ tmpList.get(i));
            if(tmpSet.size()>1){
                for (Integer key: tmpSet) {
                    mapVersion2.remove(key);
                }
            }
        }
        writer.close();
    }

    private Set<Integer> getKeysByValue(Map<Integer,String> map, String value){
        Set<Integer> keys = new HashSet<Integer>();
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                keys.add(entry.getKey());
            }
        }
        return keys;
    }


    private String padAgain(String result)
    {
        int pad = padding-result.length();
        String str = result;
        for(int i = pad; i >= 0; i--)
        {
            str +=" ";
        }
        return str;
    }

}

