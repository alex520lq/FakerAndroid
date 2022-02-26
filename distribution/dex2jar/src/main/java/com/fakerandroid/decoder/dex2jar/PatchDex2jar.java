package com.fakerandroid.decoder.dex2jar;

import com.googlecode.d2j.converter.IR2JConverter;
import com.googlecode.d2j.dex.*;
import com.googlecode.d2j.node.DexFileNode;
import com.googlecode.d2j.node.DexMethodNode;
import com.googlecode.d2j.reader.BaseDexFileReader;
import com.googlecode.d2j.reader.DexFileReader;
import com.googlecode.d2j.reader.zip.ZipUtil;
import com.googlecode.dex2jar.ir.IrMethod;
import com.googlecode.dex2jar.ir.stmt.LabelStmt;
import com.googlecode.dex2jar.ir.stmt.Stmt;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.*;
import java.nio.file.spi.FileSystemProvider;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PatchDex2jar {
    private DexExceptionHandler exceptionHandler;
    private final BaseDexFileReader reader;
    private int readerConfig;
    private int v3Config;

    public static PatchDex2jar from(byte[] in) throws IOException {
        return from((BaseDexFileReader)(new DexFileReader(ZipUtil.readDex(in))));
    }

    public static PatchDex2jar from(ByteBuffer in) throws IOException {
        return from((BaseDexFileReader)(new DexFileReader(in)));
    }

    public static PatchDex2jar from(BaseDexFileReader reader) {
        return new PatchDex2jar(reader);
    }

    public static PatchDex2jar from(File in) throws IOException {
        return from(Files.readAllBytes(in.toPath()));
    }

    public static PatchDex2jar from(InputStream in) throws IOException {
        return from((BaseDexFileReader)(new DexFileReader(in)));
    }

    public static PatchDex2jar from(String in) throws IOException {
        return from(new File(in));
    }

    private PatchDex2jar(BaseDexFileReader reader) {
        this.reader = reader;
        this.readerConfig |= 1;
    }

    private void doTranslate(final Path dist) throws IOException {
        DexFileNode fileNode = new DexFileNode();

        try {
            this.reader.accept(fileNode, this.readerConfig | 32);
        } catch (Exception var4) {
            this.exceptionHandler.handleFileException(var4);
        }

        ClassVisitorFactory cvf = new ClassVisitorFactory() {
            public ClassVisitor create(String name) {
                final ClassWriter cw = new ClassWriter(1);
                final LambadaNameSafeClassAdapter rca = new LambadaNameSafeClassAdapter(cw);
                return new ClassVisitor(327680, rca) {
                    public void visitEnd() {
                        super.visitEnd();
                        String className = rca.getClassName();

                        byte[] data;
                        try {
                            data = cw.toByteArray();
                        } catch (Exception var6) {
                            System.err.println(String.format("ASM fail to generate .class file: %s", className));
                            PatchDex2jar.this.exceptionHandler.handleFileException(var6);
                            return;
                        }

                        try {
                            Path dist1 = dist.resolve(className + ".class");
                            Path parent = dist1.getParent();
                            if (parent != null && !Files.exists(parent, new LinkOption[0])) {
                                Files.createDirectories(parent);
                            }

                            Files.write(dist1, data, new OpenOption[0]);
                        } catch (IOException var5) {
                            var5.printStackTrace(System.err);
                        }

                    }
                };
            }
        };
        (new ExDex2Asm(this.exceptionHandler) {
            public void convertCode(DexMethodNode methodNode, MethodVisitor mv) {
                if ((PatchDex2jar.this.readerConfig & 4) == 0 || !methodNode.method.getName().equals("<clinit>")) {
                    super.convertCode(methodNode, mv);
                }
            }

            public void optimize(IrMethod irMethod) {
                T_cleanLabel.transform(irMethod);
                if (0 != (PatchDex2jar.this.v3Config & 2)) {
                }

                T_deadCode.transform(irMethod);
                T_removeLocal.transform(irMethod);
                T_removeConst.transform(irMethod);
                T_zero.transform(irMethod);
                if (T_npe.transformReportChanged(irMethod)) {
                    T_deadCode.transform(irMethod);
                    T_removeLocal.transform(irMethod);
                    T_removeConst.transform(irMethod);
                }

                T_new.transform(irMethod);
                T_fillArray.transform(irMethod);
                T_agg.transform(irMethod);
                T_multiArray.transform(irMethod);
                T_voidInvoke.transform(irMethod);
                if (0 != (PatchDex2jar.this.v3Config & 4)) {
                    int i = 0;
                    Iterator var3 = irMethod.stmts.iterator();

                    while(var3.hasNext()) {
                        Stmt p = (Stmt)var3.next();
                        if (p.st == Stmt.ST.LABEL) {
                            LabelStmt labelStmt = (LabelStmt)p;
                            labelStmt.displayName = "L" + i++;
                        }
                    }

                    System.out.println(irMethod);
                }

                T_type.transform(irMethod);
                T_unssa.transform(irMethod);
                T_ir2jRegAssign.transform(irMethod);
                T_trimEx.transform(irMethod);
            }

            public void ir2j(IrMethod irMethod, MethodVisitor mv) {
                (new IR2JConverter(0 != (8 & PatchDex2jar.this.v3Config))).convert(irMethod, mv);
            }
        }).convertDex(fileNode, cvf);
    }

    public DexExceptionHandler getExceptionHandler() {
        return this.exceptionHandler;
    }

    public BaseDexFileReader getReader() {
        return this.reader;
    }

    public PatchDex2jar reUseReg(boolean b) {
        if (b) {
            this.v3Config |= 1;
        } else {
            this.v3Config &= -2;
        }

        return this;
    }

    public PatchDex2jar topoLogicalSort(boolean b) {
        if (b) {
            this.v3Config |= 2;
        } else {
            this.v3Config &= -3;
        }

        return this;
    }

    public PatchDex2jar noCode(boolean b) {
        if (b) {
            this.readerConfig |= 132;
        } else {
            this.readerConfig &= -133;
        }

        return this;
    }

    public PatchDex2jar optimizeSynchronized(boolean b) {
        if (b) {
            this.v3Config |= 8;
        } else {
            this.v3Config &= -9;
        }

        return this;
    }

    public PatchDex2jar printIR(boolean b) {
        if (b) {
            this.v3Config |= 4;
        } else {
            this.v3Config &= -5;
        }

        return this;
    }

    public PatchDex2jar reUseReg() {
        this.v3Config |= 1;
        return this;
    }

    public PatchDex2jar optimizeSynchronized() {
        this.v3Config |= 8;
        return this;
    }

    public PatchDex2jar printIR() {
        this.v3Config |= 4;
        return this;
    }

    public PatchDex2jar topoLogicalSort() {
        this.v3Config |= 2;
        return this;
    }

    public void setExceptionHandler(DexExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    public PatchDex2jar skipDebug(boolean b) {
        if (b) {
            this.readerConfig |= 1;
        } else {
            this.readerConfig &= -2;
        }

        return this;
    }

    public PatchDex2jar skipDebug() {
        this.readerConfig |= 1;
        return this;
    }

    public void to(Path file) throws IOException {
        if (Files.exists(file, new LinkOption[0]) && Files.isDirectory(file, new LinkOption[0])) {
            this.doTranslate(file);
        } else {
            FileSystem fs = createZip(file);
            Throwable var3 = null;

            try {
                this.doTranslate(fs.getPath("/"));
            } catch (Throwable var12) {
                var3 = var12;
                throw var12;
            } finally {
                if (fs != null) {
                    if (var3 != null) {
                        try {
                            fs.close();
                        } catch (Throwable var11) {
                            var3.addSuppressed(var11);
                        }
                    } else {
                        fs.close();
                    }
                }

            }
        }

    }

    private static FileSystem createZip(Path output) throws IOException {
        Map<String, Object> env = new HashMap();
        env.put("create", "true");
//        Files.deleteIfExists(output);
        Path parent = output.getParent();
        if (parent != null && !Files.exists(parent, new LinkOption[0])) {
            Files.createDirectories(parent);
        }

        Iterator var3 = FileSystemProvider.installedProviders().iterator();

        FileSystemProvider p;
        String s;
        do {
            if (!var3.hasNext()) {
                throw new IOException("cant find zipfs support");
            }

            p = (FileSystemProvider)var3.next();
            s = p.getScheme();
        } while(!"jar".equals(s) && !"zip".equalsIgnoreCase(s));

        return p.newFileSystem(output, env);
    }

    public PatchDex2jar withExceptionHandler(DexExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
        return this;
    }

    public PatchDex2jar skipExceptions(boolean b) {
        if (b) {
            this.readerConfig |= 256;
        } else {
            this.readerConfig &= -257;
        }

        return this;
    }
}
