

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h> 
#include <sys/socket.h>
#include <netinet/in.h>
#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#define PORT_NUMBER 9383

#include < stdio.h >



// Helper function for error messages (not required)
void error(const char *msg)
{
    perror(msg);
    exit(1);
}
// this is all stuff for the hashtable
struct node{
    int key;
    int val;
    struct node *next;
};
struct table{
    int size;
    struct node **list;
};
struct table *createTable(int size){
    struct table *t = (struct table*)malloc(sizeof(struct table));
    t->size = size;
    t->list = (struct node**)malloc(sizeof(struct node*)*size);
    int i;
    for(i=0;i<size;i++)
        t->list[i] = NULL;
    return t;
}
int hashCode(struct table *t,int key){
    if(key<0)
        return -(key%t->size);
    return key%t->size;
}
void insert(struct table *t,int key,int val){
    int pos = hashCode(t,key);
    struct node *list = t->list[pos];
    struct node *newNode = (struct node*)malloc(sizeof(struct node));
    struct node *temp = list;
    while(temp){
        if(temp->key==key){
            temp->val = val;
            return;
        }
        temp = temp->next;
    }
    newNode->key = key;
    newNode->val = val;
    newNode->next = list;
    t->list[pos] = newNode;
}
int lookup(struct table *t,int key){
    int pos = hashCode(t,key);
    struct node *list = t->list[pos];
    struct node *temp = list;
    while(temp){
        if(temp->key==key){
            return temp->val;
        }
        temp = temp->next;
    }
    return -1;
}

void printRandoms(int lower, int upper, 
                             int count)
{
    int i;
    for (i = 0; i < count; i++) {
        int num = (rand() %
           (upper - lower + 1)) + lower;
        printf("%d ", num);
    }
}

int main(int argc, char *argv[])
{
	// Prepare for socket communication
	int sockfd, newsockfd, portno = PORT_NUMBER;
	char words[2000]; // words user puts in
	char syllables[1000];//syllables
	int count = 0;
	socklen_t clilen;
	char buffer[256];
	char haiku[500];
	struct sockaddr_in serv_addr, cli_addr;
	int n; // Number of bytes written/read

	FILE *fr;

    char line[1000];

    fr = fopen ("syllabletxt", "r");  /* open the file for reading syllable file*/

    while(fgets(line, 1000, fr) != NULL)
    {
    /* get a line, up to 80 chars from fr.  done if NULL */
    sscanf  ( line );
    }
    fclose(fr);  /* close the file prior to exiting the routine */

	sockfd = socket(AF_INET, SOCK_STREAM, 0);
	if (sockfd < 0) 
		error("ERROR opening socket");
	bzero((char *) &serv_addr, sizeof(serv_addr));
	serv_addr.sin_family = AF_INET;
	serv_addr.sin_addr.s_addr = INADDR_ANY;
	serv_addr.sin_port = htons(portno);
	if (bind(sockfd, (struct sockaddr *) &serv_addr, sizeof(serv_addr)) < 0) 
		error("ERROR on binding");
	printf("Listening for client...\n"); fflush(stdout);
	listen(sockfd, 5);
	clilen = sizeof(cli_addr);
	newsockfd = accept(sockfd, (struct sockaddr *) &cli_addr, &clilen);
	if (newsockfd < 0) 
		error("ERROR on accept");
	bzero(buffer, sizeof(buffer));

	// Send question to client
	sprintf(buffer, "Excluded words?");
	n = write(newsockfd, buffer, strlen(buffer));
	if (n < 0) error("ERROR reading from socket");

	// Receive answer from client
	bzero(buffer, sizeof(buffer));
	n = read(newsockfd, buffer, sizeof(buffer));
	printf("Server received: %s\n", buffer); fflush(stdout); // Not required

	// Check client's response and send reply
	if (strcmp(buffer, "Yes") == 0) {
		bzero(buffer, sizeof(buffer));
		sprintf(buffer, "Alright!");
	} else {
		bzero(buffer, sizeof(buffer));
		sprintf(buffer, "No? Okay goodbye!");
		close(newsockfd);
		close(sockfd);
		return 0;
	}


// Randomly generate a number < 15. This will be the number of syllables we start with.
    int lower = 0, upper = 15, count = 1;
 
    printRandoms(lower, upper, count);
 

	// Compute a haiku with a hashtable Needs to be done but how?
	//The server will receive the text file, sanitize/standardize the input, and then add each word to a hashmap with the syllable count as the key. When requested, the algorithm for creating a haiku will be:
 	/* 
 	We add this word and syllable count to another hashmap.
 	 We have a pointer that keeps track of the current syllable count. 
 	 We increment the pointer. 
 	 If the current syllable total + this syllable count is over 15, we decrement the pointer until we reach a syllable count that is 15 or less. 
 	 Once we have been able to reach 15 syllables with words (or after we reach a syllable count of 1 and are unable to add new words to reach 15),
 	  we have a hashmap containing words that sum to 15 syllables. We iterate through the hashmap and write each word to the buffer. 
 	  We write the resulting haiku to the shared memory.  
		If there is an included/excluded words list, the included words will be selected first. For the excluded words list, each word in that list will be added to the hashset. 
		As each word in the source.txt is parsed and added to the hashmap, 
		it will be checked that the word is not on the excluded list. 
		If it is in the excluded list, it will not be added to the hashmap containing syllable counts.
		*/

 	//Note: we assume that the words in a haiku can span line breaks, as long as the number of syllables if 15. We will not be inserting hyphenation.


	


	printf("Haiku"); fflush(stdout); // Print's out the haiku


	n = write(newsockfd, buffer, sizeof(buffer));

	// Cleanup
	close(newsockfd);
	close(sockfd);
    return 0; 
}
