package folders.comperator;

import UI.FolderComparatorGui;

import java.io.*;
import java.security.MessageDigest;
import java.util.*;

public class ComparingFolders {
    //This can be any folder locations which you want to compare
    private File oldVersion; //the folder of the old version package
    private File newVersion; //the folder of the new version package
    private String destenationPath; //the folder of the results log (the user chooses)
    private String oldVersionPath; //the location of the old version package
    private String newVersionPath; //the location of the old version package
    private final int padding = 50;


    /**
     *
     * @param OldVersionPath - a path to the old version package
     * @param newVersionPath - a path to the new version package
     * @param destenationPath -a path to the results logs of versions package
     * @throws IOException - can throw IOException if not found
     */
    public ComparingFolders(String OldVersionPath , String newVersionPath , String destenationPath) throws IOException{
        oldVersion = new File(OldVersionPath);
        newVersion = new File(newVersionPath);
        this.destenationPath=destenationPath;
        File directory = new File(destenationPath);
        if (! directory.exists()){
            directory.mkdir();
        }
        PrintWriter writer = new PrintWriter(new FileOutputStream(
                new File(destenationPath+"\\Changes in folders log.txt"),
                false /* append = true */));
        writer.close();
        this.oldVersionPath =OldVersionPath;
        this.newVersionPath=newVersionPath;
        getDiff(oldVersion , newVersion);

    }

    private void getDiff(File oldVersion, File newVersion) throws IOException
    {
        File[] oldList = oldVersion.listFiles();
        File[] newList = newVersion.listFiles();
        HashMap<String, File> map1;
        if(oldList.length < newList.length)
        {
            map1 = new HashMap<String, File>();
            for(int i=0;i<oldList.length;i++)
            {
                if(oldList[i].getName().contains(".bar")){
                   String fName = oldList[i].getName().substring(0 , oldList[i].getName().indexOf("_")) + oldList[i].getName().substring(oldList[i].getName().indexOf(".") , oldList[i].getName().length());
                   map1.put(fName,oldList[i]);
                }
                else{
                    map1.put(oldList[i].getName(),oldList[i]);

                }
            }
            compareNow(newList, map1);
        }
        else
        {
            map1 = new HashMap<String, File>();
            for(int i=0;i<newList.length;i++)
            {
                if(oldList[i].getName().contains(".bar")){
                    String fName = oldList[i].getName().substring(0 , oldList[i].getName().indexOf("_")) + oldList[i].getName().substring(oldList[i].getName().indexOf(".") , oldList[i].getName().length());
                    map1.put(fName,newList[i]);

                }
                else{
                    map1.put(newList[i].getName(),newList[i]);
                }
            }
            compareNow(oldList, map1);
        }
    }

    private void compareNow(File[] fileArr, HashMap<String, File> map) throws IOException
    {

//        String version = "new Version";
//        if(fileArr[0].getAbsolutePath().contains(oldVersion.getAbsolutePath())){
//            version = "old Version";
//        }
        PrintWriter writer = new PrintWriter(new FileOutputStream(
                new File(destenationPath+"\\Changes in folders log.txt"),
                true /* append = true */));
        for(int i=0;i<fileArr.length;i++)
        {
            String fName = fileArr[i].getName();
            if(fName.contains(".bar")){
                fName = fName.substring(0 , fName.indexOf("_")) + fName.substring(fName.indexOf(".") , fName.length());
            }
            File fComp = map.get(fName);
            map.remove(fName);
            if(fComp!=null)
            {
                if(fComp.isDirectory())
                {
                    getDiff(fileArr[i], fComp);
                }
                else
                {
                    String cSum1 = checksum(fileArr[i]);
                    String cSum2 = checksum(fComp);
                    if(!cSum1.equals(cSum2))
                    {
                        if(!isIgnoreFileType(fName)){
                            String version;
                            if (fComp.getAbsolutePath().contains(this.oldVersionPath)){
                                version = this.oldVersionPath;
                                }
                            else{
                                version=this.newVersionPath;
                            }
                            String tmp = fComp.getAbsolutePath().replace(version,"").replace("\\" , "_");
                            ComparingFiles display = new ComparingFiles(fileArr[i].getAbsolutePath() , fComp.getAbsolutePath(),destenationPath+"\\"+tmp+".txt" , this.oldVersionPath , this.newVersionPath );
                            boolean isNewVersion;
                            writer.println(padAgain(fileArr[i].getName())+ "Different");
                         }
                         else{
                            writer.println(padAgain(fileArr[i].getName())+ "Different");
                         }
                    }

                    // this else will print that files are "identical"

//                    else
//                    {
//                        System.out.println(fileArr[i].getName()+"\t\t"+"identical");
//                          writer.println(fileArr[i].getName()+"\t\t"+"identical");
//                    }
                }
            }
            else
            {
                if(fileArr[i].isDirectory())
                {
                    traverseDirectory(fileArr[i]);
                    System.out.println(fileArr[i]);
                    if(fileArr[i].getAbsolutePath().contains(newVersionPath)){

                    }
                    writer.println(padAgain("<FOLDER>"+fileArr[i].getName()+"<FOLDER> ")+ "Only in "+fileArr[i].getParent());
                }
                else
                {
                    writer.println(padAgain(fileArr[i].getName())+"Only in "+fileArr[i].getAbsolutePath());
                }
            }
        }
        Set<String> set = map.keySet();
        Iterator<String> it = set.iterator();
        List<String> toRemove = new ArrayList<String>();
        while(it.hasNext())
        {
            String n = it.next();
            File fileFrmMap = map.get(n);
            toRemove.add(n);
            if(fileFrmMap.isDirectory())
            {
                traverseDirectory(fileFrmMap);
            }
            else
            {
                writer.println(padAgain(fileFrmMap.getName())+"Only in "+ fileFrmMap.getAbsolutePath());
            }
        }
        for (String s : toRemove){
            if (map.containsKey(s)){
                map.remove(s);
            }
        }
        writer.close();
    }

    private void traverseDirectory(File dir) throws IOException
    {
        PrintWriter writer = new PrintWriter(new FileOutputStream(
                new File(destenationPath+"\\Changes in folders log.txt"),
                true /* append = true */));

        File[] list = dir.listFiles();
        for(int k=0;k<list.length;k++)
        {
            if(list[k].isDirectory())
            {
                traverseDirectory(list[k]);
            }
            else
            {
                writer.println(padAgain(list[k].getName())+"Only in "+ list[k].getAbsolutePath());
            }
        }
        writer.close();
    }

    private String checksum(File file)
    {
        try
        {
            InputStream fin = new FileInputStream(file);
            MessageDigest md5er = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[1024];
            int read;
            do
            {
                read = fin.read(buffer);
                if (read > 0)
                    md5er.update(buffer, 0, read);
            } while (read != -1);
            fin.close();
            byte[] digest = md5er.digest();
            if (digest == null)
                return null;
            String strDigest = "0x";
            for (int i = 0; i < digest.length; i++)
            {
                strDigest += Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1).toUpperCase();
            }
            return strDigest;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    private boolean isIgnoreFileType(String fileName){

        if(fileName.contains(".bar") || fileName.contains(".jar") || fileName.contains(".dll"))
            return true;
        else
            return false;

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