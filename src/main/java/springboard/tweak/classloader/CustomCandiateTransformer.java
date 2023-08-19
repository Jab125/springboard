package springboard.tweak.classloader;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.Optional;

public class CustomCandiateTransformer implements ClassTransformer {
    @Override
    public boolean shouldTransform(String name) {
        return name.startsWith("springboard.board.CustomCandidateFinder");
    }

    @Override
    public byte[] transformClass(String className, byte[] in) {
        ClassReader classReader = new ClassReader(in);
        ClassNode node = new ClassNode(Opcodes.ASM9);
        classReader.accept(node, ClassReader.SKIP_DEBUG + ClassReader.SKIP_FRAMES);
        for (InnerClassNode innerClass : node.innerClasses) {
            if (innerClass.name != null) innerClass.name = innerClass.name.replaceAll("2", "");
            if (innerClass.outerName != null) innerClass.outerName = innerClass.outerName.replaceAll("2", "");
            if (innerClass.innerName != null) innerClass.innerName = innerClass.innerName.replaceAll("2", "");
        }
        if (node.interfaces.size() > 0) node.interfaces.set(0, node.interfaces.get(0).replaceAll("2", ""));

        for (MethodNode method : node.methods) {
            method.desc = method.desc.replaceAll("2", "");
            for (AbstractInsnNode instruction : method.instructions) {
                if (instruction instanceof MethodInsnNode) {
                    MethodInsnNode methodInsnNode = (MethodInsnNode) instruction;
                    methodInsnNode.owner = methodInsnNode.owner.replaceAll("2", "");
                }
            }
        }

        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES + ClassWriter.COMPUTE_MAXS);
        node.accept(classWriter);
        return classWriter.toByteArray();
    }
}
