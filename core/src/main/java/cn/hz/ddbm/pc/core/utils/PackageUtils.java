package cn.hz.ddbm.pc.core.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

public class PackageUtils {

    //扫描一般的包。
    private static Set<Class> scanPackage(String packageName, File currentfile) {
        HashSet<Class> types = new HashSet<>();
        File[] filelist = currentfile.listFiles(new FileFilter() {
            //FileFilter是文件过滤器,源代码只写了一个accapt的抽象方法。
            @Override
            public boolean accept(File pathName) {
                if (pathName.isDirectory()) {    //判断是否是目录
                    return true;
                }
                return pathName.getName().endsWith(".class");
            }
        });

        for (File file : filelist) {
            if (file.isDirectory()) {
                types.addAll(scanPackage(packageName + "." + file.getName(), file));
            } else {
                String fileName  = file.getName().replace(".class", "");
                String className = packageName + "." + fileName;
                try {
                    Class<?> klass = Class.forName(className);//取出所有的类
                    if (klass.isAnnotation() //不扫描注解类、枚举类、接口和八大基本类型。
                            || klass.isEnum()
                            || klass.isInterface()
                            || klass.isPrimitive()) {
                        continue;
                    }
                    types.add(klass);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return types;
    }

    //扫描jar包方法。
    private static HashSet<Class> scanPackage(URL url) throws IOException {
        HashSet<Class>        types         = new HashSet<>();
        JarURLConnection      urlConnection = (JarURLConnection) url.openConnection();
        JarFile               jarfile       = urlConnection.getJarFile();
        Enumeration<JarEntry> jarEntries    = jarfile.entries();
        while (jarEntries.hasMoreElements()) {
            JarEntry jarEntry = jarEntries.nextElement();
            String   jarName  = jarEntry.getName();
            if (!jarName.endsWith(".class")) {
                continue;
            }
            String className = jarName.replace(".class", "").replaceAll("/", ".");

            try {
                Class<?> klass = Class.forName(className);
                if (klass.isAnnotation()
                        || klass.isInterface()
                        || klass.isEnum()
                        || klass.isPrimitive()) {
                    continue;
                }
                types.add(klass);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return types;
    }


    //用包名进行扫描
    public static HashSet<Class> scan(String packageName) {
        HashSet<Class> types         = new HashSet<>();
        String         packOpperPath = packageName.replace(".", "/");

        //线程上下文类加载器得到当前的classpath的绝对路径.（动态加载资源）
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        try {
            //(Thread.currentThread().getContextClassLoader().getResource(""))
            //(来得到当前的classpath的绝对路径的URI表示法。)
            Enumeration<URL> resources = classloader.getResources(packOpperPath);
            while (resources.hasMoreElements()) {
                //先获得本类的所在位置
                URL url = resources.nextElement();

                //url.getProtocol()是获取URL的HTTP协议。
                if (url.getProtocol().equals("jar")) {
                    //判断是不是jar包
                    types.addAll(scanPackage(url));
                } else {
                    //此方法不会自动将链接中的非法字符转义。
                    //而在File转化成URI的时候，会将链接中的特殊字符如#或!等字符进行编码。
                    File file = new File(url.toURI());
                    if (!file.exists()) {
                        continue;
                    }
                    types.addAll(scanPackage(packageName, file));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return types;
    }

    public static Set<Class> scan(String packageName, Class... itfs) {
        Set<Class> types = Arrays.stream(itfs).collect(Collectors.toSet());
        return scan(packageName).stream().filter(c -> types.stream().anyMatch(t -> t.isAssignableFrom(c))).collect(Collectors.toSet());
    }


}
