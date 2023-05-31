# Kotlin Persistent Data Structures Library
The library provides Kotlin implementations of persistent data structures.

The main feature of this library is the universal model of persistent data, by which any graph can be made partially or full persistent.



## Links
1. [Partial Persistent Model](#partial-persistent-model)
    * [Example. Partial Persistent List](#example1)
2. [Full Persistent Model](#full-persistent-model)
    1. [List Order Maintenance](#list-order-maintenance)
    2. [Scapegoat Tree](#scapegoat-tree)
    3. [Y-Fast Trie](#fast-trie)
3. [Articles](#articles)

## Partial Persistent Model
Algorithm of the partial persistent model:

![Screenshot 2023-05-31 at 14 23 21](https://github.com/BagritsevichStepan/persistent-data-structures-library/assets/43710058/204a690f-2da4-4f08-9e5e-b36a39828785)

TODO: translate in english

### <a name="example1"></a>Example. Partial Persistent List
Implementation of Partial Persistent List using partial persistent model. All operations in O(1).

![Screenshot 2023-05-31 at 14 32 42](https://github.com/BagritsevichStepan/persistent-data-structures-library/assets/43710058/2f1b5bff-60c1-4991-b052-e760d6d66957)

![Screenshot 2023-05-31 at 14 33 23](https://github.com/BagritsevichStepan/persistent-data-structures-library/assets/43710058/2bef04f9-a943-454b-b9f1-c0aa077fe759)

![Screenshot 2023-05-31 at 14 33 40](https://github.com/BagritsevichStepan/persistent-data-structures-library/assets/43710058/1ed9c683-0c8a-457d-9901-486935deedcb)


## Full Persistent Model
Algorithm of the full persistent model:

![Screenshot 2023-05-31 at 14 44 27](https://github.com/BagritsevichStepan/persistent-data-structures-library/assets/43710058/fe6b5e6f-a9f2-4a89-bc1d-0f9caedf2fa1)

![Screenshot 2023-05-31 at 14 44 44](https://github.com/BagritsevichStepan/persistent-data-structures-library/assets/43710058/77fdb4c9-8e19-4bda-9eb7-2056767c4a2c)

![Screenshot 2023-05-31 at 14 44 54](https://github.com/BagritsevichStepan/persistent-data-structures-library/assets/43710058/a7eb1269-3d13-4e14-a9e7-0015cb265fb0)


### List Order Maintenance
In computer science, the order-maintenance problem involves maintaining a totally ordered set supporting the following operations:
* insert(X, Y), which inserts X immediately after Y in the total order;
* order(X, Y), which determines if X precedes Y in the total order; and
* delete(X), which removes X from the set.

To implement list order maintenance you must use **Scapegoat Tree** or **Y-Fast-Trie**

### Scapegoat Tree
In computer science, a scapegoat tree is a self-balancing binary search tree. It provides worst-case O(log n) lookup time (with n as the number of entries) and O(log n) amortized insertion and deletion time.

### <a name="fast-trie"></a>Y-Fast-Trie
In computer science, a y-fast trie is a data structure for storing integers from a bounded domain. It supports exact and predecessor or successor queries in time O(log log M), using O(n) space, where n is the number of stored values and M is the maximum value in the domain.

## Articles
Lecture by Pavel Mavrin in Computer Science Center (in Russian): [youtube.com](https://www.youtube.com/watch?v=cSiiVofy2Tw)

Article about persistent model (in Russian): [neerc.ifmo.ru](https://neerc.ifmo.ru/wiki/index.php?title=Персистентные_структуры_данных)

Articles about list order maitenance:
1. [in Russian](https://neerc.ifmo.ru/wiki/index.php?title=List_order_maintenance)
2. [in English](https://en.wikipedia.org/wiki/Order-maintenance_problem#:~:text=In%20computer%20science%2C%20the%20order,removes%20X%20from%20the%20set.)

Articles about scapegoat tree:
1. [in Russian](https://neerc.ifmo.ru/wiki/index.php?title=Scapegoat_Tree)
2. [in English](https://en.wikipedia.org/wiki/Scapegoat_tree)

Articles about y-fast-trie:
1. [in Russian](https://neerc.ifmo.ru/wiki/index.php?title=Сверхбыстрый_цифровой_бор)
2. [in English](https://en.wikipedia.org/wiki/Y-fast_trie)

