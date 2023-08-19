package springboard.tweak.classloader;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import springboard.tweak.classloader.ClassTransformer;

import java.util.Optional;

public class FabricLoaderImplTransformer implements ClassTransformer {
    @Override
    public boolean shouldTransform(String name) {
        return "net.fabricmc.loader.impl.FabricLoaderImpl".equals(name);
    }

    @Override
    public byte[] transformClass(String className, byte[] in) {
        ClassReader classReader = new ClassReader(in);
        ClassNode node = new ClassNode(Opcodes.ASM9);
        classReader.accept(node, ClassReader.SKIP_DEBUG + ClassReader.SKIP_FRAMES);
        Optional<MethodNode> g = node.methods.stream().filter(a -> "setup".equals(a.name)).findFirst();
        MethodNode methodNode = g.orElseThrow(NullPointerException::new); // INVOKEVIRTUAL net/fabricmc/loader/impl/discovery/ModDiscoverer.addCandidateFinder (Lnet/fabricmc/loader/impl/discovery/ModCandidateFinder;)V
        MethodInsnNode node2 = null;
        for (AbstractInsnNode instruction : methodNode.instructions) {
            if (instruction instanceof MethodInsnNode) {
                MethodInsnNode methodInsnNode = (MethodInsnNode) instruction;
                if ("addCandidateFinder".equals(methodInsnNode.name)) {
                    // this is the one
                    node2 = methodInsnNode;
                }
            }
        }
        if (node2 == null) throw new NullPointerException();
        InsnList list = new InsnList();
        list.add(new VarInsnNode(Opcodes.ALOAD, 4));
        list.add(new TypeInsnNode(Opcodes.NEW, "springboard/board/CustomCandidateFinder"));
        list.add(new InsnNode(Opcodes.DUP));
        list.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "springboard/board/CustomCandidateFinder", "<init>", "()V"));
        list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/fabricmc/loader/impl/discovery/ModDiscoverer", "addCandidateFinder", "(Lnet/fabricmc/loader/impl/discovery/ModCandidateFinder;)V"));
        methodNode.instructions.insert(node2, list);

        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES + ClassWriter.COMPUTE_MAXS);
        node.accept(classWriter);
        return classWriter.toByteArray();
    }
}
