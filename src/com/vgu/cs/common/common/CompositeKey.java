
package com.vgu.cs.common.common;

/**
 *
 * @author namnh16
 */
public class CompositeKey {

    public static class Three<T1, T2, T3> implements Comparable<Three<T1, T2, T3>> {

        public static final SimpleObjectPool<Three> OBJECT_POOL = new SimpleObjectPool<Three>(
                "CompositeKey.Three",
                0, 
                () -> new Three<>(),
                null, 
                (Three object) -> {
                    object.value1 = null;
                    object.value2 = null;
                    object.value3 = null;
                    
                    return object;
                }
        );

        public T1 value1;
        public T2 value2;
        public T3 value3;

        public Three() {
        }

        public Three(T1 value1, T2 value2, T3 value3) {
            set(value1, value2, value3);
        }

        public final Three<T1, T2, T3> set(T1 value1, T2 value2, T3 value3) {
            this.value1 = value1;
            this.value2 = value2;
            this.value3 = value3;

            return this;
        }

        @Override
        public int compareTo(Three<T1, T2, T3> o) {
            if (this == o) {
                return 0;
            }

            //// do work on value1
            {
                if (this.value1 == null && o.value1 != null) {
                    return -1;
                }

                if (this.value1 != null && o.value1 == null) {
                    return +1;
                }

                if (this.value1 != null && o.value1 != null && this.value1 != o.value1) {
                    Comparable<? super T1> thisValue = (Comparable<? super T1>) this.value1;
                    int c = thisValue.compareTo(o.value1);

                    if (c != 0) {
                        return c;
                    }
                }
            }

            //// do work on value2
            {
                if (this.value2 == null && o.value2 != null) {
                    return -1;
                }

                if (this.value2 != null && o.value2 == null) {
                    return +1;
                }

                if (this.value2 != null && o.value2 != null && this.value2 != o.value2) {
                    Comparable<? super T2> thisValue = (Comparable<? super T2>) this.value2;
                    int c = thisValue.compareTo(o.value2);

                    if (c != 0) {
                        return c;
                    }
                }
            }

            //// do work on value3
            {
                if (this.value3 == null && o.value3 != null) {
                    return -1;
                }

                if (this.value3 != null && o.value3 == null) {
                    return +1;
                }

                if (this.value3 != null && o.value3 != null && this.value3 != o.value3) {
                    Comparable<? super T3> thisValue = (Comparable<? super T3>) this.value3;
                    int c = thisValue.compareTo(o.value3);

                    if (c != 0) {
                        return c;
                    }
                }
            }

            return 0;
        }

        @Override
        public int hashCode() {
            int h1 = value1 != null ? value1.hashCode() : 0;
            int h2 = value2 != null ? value2.hashCode() : 0;
            int h3 = value3 != null ? value3.hashCode() : 0;
            return h1 ^ h2 ^ h3;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }

            if (this.getClass() != obj.getClass()) {
                return false;
            }

            Three<T1, T2, T3> ano = (Three<T1, T2, T3>) obj;

            //// do work on value1
            {
                boolean eq = (this.value1 == null && ano.value1 == null)
                        || (this.value1 == ano.value1)
                        || (this.value1 != null && ano.value1 != null && this.value1.equals(ano.value1));

                if (!eq) {
                    return false;
                }
            }

            //// do work on value2
            {
                boolean eq = (this.value2 == null && ano.value2 == null)
                        || (this.value2 == ano.value2)
                        || (this.value2 != null && ano.value2 != null && this.value2.equals(ano.value2));

                if (!eq) {
                    return false;
                }
            }

            //// do work on value3
            {
                boolean eq = (this.value3 == null && ano.value3 == null)
                        || (this.value3 == ano.value3)
                        || (this.value3 != null && ano.value3 != null && this.value3.equals(ano.value3));

                if (!eq) {
                    return false;
                }
            }
            return true;
        }
    }
}
