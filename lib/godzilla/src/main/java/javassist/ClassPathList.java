package javassist;

/* compiled from: ClassPoolTail */
final class ClassPathList {
    ClassPathList next;
    ClassPath path;

    ClassPathList(ClassPath p, ClassPathList n) {
        this.next = n;
        this.path = p;
    }
}
