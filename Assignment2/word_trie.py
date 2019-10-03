"""
    Author: Ryan Flageolle
    Assignment: To design a text prediction algorithm which prompts user input
        and returns the next 10 possible words from the input given
    Resources used: Learned about the Trie from:
        https://www.geeksforgeeks.org/trie-insert-and-search/
"""
import sys, select, tty, termios


class NonBlockingConsole(object):
    """
        from: https://stackoverflow.com/questions/16547486/read-raw-input-from-
        keyboard-in-python

        use: to give me the ability to collect input and process it dynamically
    """
    def __enter__(self):
        self.old_settings = termios.tcgetattr(sys.stdin)
        tty.setcbreak(sys.stdin.fileno())
        return self

    def __exit__(self, type, value, traceback):
        termios.tcsetattr(sys.stdin, termios.TCSADRAIN, self.old_settings)

    def get_data(self):
        try:
            if select.select([sys.stdin], [], [], 0) == ([sys.stdin], [], []):
                return sys.stdin.read(1)
        except:
            return '[CTRL-C]'
        return False

class Trie:
    """Short summary.

    Parameters
    ----------
    v : string
        Description: value that is represented by that node.
    isEndOfWord : boolean
        Description: describes whether or not the node represents a complete
                     word.

    Attributes
    ----------
    children : dict
        Description: A dictionary which is initialized as empty to hold more
                     tries which represent if more letters follow it.
    value : string
        Description: Stores the parameter `v`.
    isEndOfWord
        Description: Stores the parameter `isEndOfWord`.

    """

    def __init__(self, v='', isEndOfWord=False):
        self.children = dict()
        self.value = v
        self.isEndOfWord = isEndOfWord

    def getNode(self, w):
        """
        Parameters
        ----------
        w : string
            Description: A string to be searched for in the trie.

        Returns
        -------
        Trie
            Description: The Trie representing the last letter in `w`.
        """
        curr = self
        for v in w:
            curr = curr.children.get(v, None)
            if curr is None:
                break
        return curr

    def addNode(self, v, isword):
        """
        Parameters
        ----------
        v : string
            Description: Value of the node to be added.
        isword : boolean
            Description: Value representing if the node is a full word.

        Returns
        -------
        None
            Description: Does not return, it adds a trie to `self`.

        """
        self.children[v] = Trie(self.value + v, isword)

    def insert(self, key):
        """
        Parameters
        ----------
        key : String
            Description: String to be reptesented in the trie.

        Returns
        -------
        None
            Description: Does not return, it adds a full word to `self`.

        """
        curr = self
        l = len(key)
        for idx, c in enumerate(key):
            next = curr.children.get(c, None)
            if next is None:
                isEndOfWord = idx == l-1
                curr.addNode(c, isEndOfWord)
                next = curr.children[c]
            curr = next

    def starts_with(self, key, amount=None):
        """
        Parameters
        ----------
        key : string
            Description: Value to be checked if the trie contains words
                         which contain `key` as a prefix.
        amount : int
            Description: Defaults to none, else the ammount of words that the
                         user would like to return.

        Returns
        -------
        list[string]
            Description: A list consiting of `amount` words which contain `key`
                         as a prefix.

        """
        start = self.getNode(key)
        if start is None:
            return []
        words = []
        if start.isEndOfWord:
            words.append(start.value)
        for c, child in sorted(start.children.items(), key=lambda kv: kv[0]):
            words += child.starts_with('', amount)

        if amount is None:
            return words

        return words[:amount]

def create_trie(file):
    """
    Parameters
    ----------
    file : string
        Description: URL of the text file representing the dictionary of words
                     used to build the trie.

    Returns
    -------
    Trie
        Description: A Trie object representing the dictionary.

    """
    t = Trie()

    x = 0
    with open(file) as f:
        for key in f:
            t.insert(key.lower().rstrip('\n '))
            x = x + 1

    print("successfully read in %d words from %s" % (x, file))
    return t

def __main__():
    """
    The driver method which builds the Trie and interacts with the user.
    """
    print("Loading...")
    t = create_trie('words.txt')
    word = ''
    words = ''

    print("type to start.",
          "[ESC] or [CTRL-C] to quit.",
          "Backspace works.",
          "Enter to new word",
          "\n")

    with NonBlockingConsole() as nbc:
        while 1:
            c = nbc.get_data()
            if c:
                if c == '\x1b': # x1b is ESC
                    break
                elif c == '\x7f': # backspace
                    l = len(words)
                    word = word[:-1]
                    words = word + ": " + " ".join(t.starts_with(word, 10))
                    words += " " * (l - len(words))
                    sys.stdout.write('\b' * l + words)
                    sys.stdout.flush()
                elif c == '[CTRL-C]':
                    word = ''
                    sys.stdout.write('\n')
                    break
                elif c == '\n': # it's RETURN
                    sys.stdout.write('\n')
                    # parse data here
                    word = ''
                else:
                    l = len(words)
                    word += (c)
                    words = word + ": " + " ".join(t.starts_with(word, 10))
                    words += " " * (l - len(words))
                    sys.stdout.write('\b' * l + words)
                    sys.stdout.flush()

__main__()
