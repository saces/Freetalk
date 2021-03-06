/* This code is part of Freenet. It is distributed under the GNU General
 * Public License, version 2 (or at your option any later version). See
 * http://www.gnu.org/ for further details of the GPL. */
package plugins.Freetalk.WoT;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import plugins.Freetalk.Board;
import plugins.Freetalk.DatabaseBasedTest;
import plugins.Freetalk.Freetalk;
import plugins.Freetalk.Message;
import plugins.Freetalk.MessageList;
import plugins.Freetalk.MessageManager;
import plugins.Freetalk.Message.Attachment;
import freenet.keys.FreenetURI;


public class WoTMessageXMLTest extends DatabaseBasedTest {
	
	private Freetalk mFreetalk;
	private MessageManager mMessageManager;
	private FreenetURI mMessageRealURI;
	
	private String mMessageListID;
	private String mMessageID;
	
	private String mHardcodedEncodedMessage;
		
	public void setUp() throws Exception {
		super.setUp();
		
		mFreetalk = new Freetalk(db);
		mMessageManager = mFreetalk.getMessageManager();
		
		Board myBoard = mMessageManager.getOrCreateBoard("en.board1");
		HashSet<Board> myBoards = new HashSet<Board>();
		myBoards.add(myBoard);
		myBoards.add(mMessageManager.getOrCreateBoard("en.board2"));
		
		FreenetURI authorRequestSSK = new FreenetURI("SSK@nU16TNCS7~isPTa9gw6nF8c3lQpJGFHA2KwTToMJuNk,FjCiOUGSl6ipOE9glNai9WCp1vPM8k181Gjw62HhYSo,AQACAAE/");
		FreenetURI authorInsertSSK = new FreenetURI("SSK@Ykhv0x0K8jtrgOlqWVS4S2Jvmnm64zv5voNjMfz1nYI,FjCiOUGSl6ipOE9glNai9WCp1vPM8k181Gjw62HhYSo,AQECAAE/");
		WoTIdentity myAuthor = new WoTOwnIdentity(WoTIdentity.getIDFromURI(authorRequestSSK), authorRequestSSK, authorInsertSSK, "Nickname");
		myAuthor.initializeTransient(mFreetalk);
		myAuthor.storeAndCommit();
		
		FreenetURI myThreadRealURI = new FreenetURI("CHK@7qMS7LklYIhbZ88i0~u97lxrLKS2uxNwZWQOjPdXnJw,IlA~FSjWW2mPWlzWx7FgpZbBErYdLkqie1uSrcN~LbM,AAIA--8");
		String myThreadID = "afe6519b-7fb2-4533-b172-1f966e79d127" + "@" + myAuthor.getID();
		
		mMessageRealURI = new FreenetURI("CHK@7qMS7LklYIhbZ88i0~u97lxrLKS2uxNwZWQOjPdXnJw,IlA~FSjWW2mPWlzWx7FgpZbBErYdLkqie1uSrcN~LbM,AAIA--8");
		String myMessageID = "2a3a8e7e-9e53-4978-a8fd-17b2d92d949c" + "@" + myAuthor.getID();
		
		List<MessageList.MessageReference> messageReferences = new ArrayList<MessageList.MessageReference>(2);
		for(Board board : myBoards) {
			messageReferences.add(new MessageList.MessageReference(myThreadID, myThreadRealURI, board));
			messageReferences.add(new MessageList.MessageReference(myMessageID, mMessageRealURI, board));
		}
		WoTMessageList messageList = new WoTMessageList(myAuthor, WoTMessageList.assembleURI(authorRequestSSK, 123), messageReferences);
		messageList.initializeTransient(mFreetalk);
		messageList.storeWithoutCommit();
		db.commit();
		mMessageListID = messageList.getID();
		
		List<Attachment> attachments = new ArrayList<Attachment>();
		attachments.add(new Attachment(new FreenetURI("KSK@attachment1"), 10001));
		attachments.add(new Attachment(new FreenetURI("KSK@attachment2"), 10002));
		
		WoTMessage message = WoTMessage.construct(messageList, mMessageRealURI, myMessageID, new WoTMessageURI(messageList.getURI(), myMessageID),
				new WoTMessageURI(messageList.getURI(), myThreadID), myBoards, myBoard, myAuthor,
				"Message title", new Date(109, 4, 3, 16, 15, 14), "Message body\nNew line", attachments);
		
		mMessageID = message.getID();
		
		message.initializeTransient(mFreetalk);
		message.storeAndCommit();
		

		mHardcodedEncodedMessage = new String(
			"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
			"<Freetalk-testing>\n" +
			"<Message version=\"1\">\n" +
			"<MessageID><![CDATA[2a3a8e7e-9e53-4978-a8fd-17b2d92d949c@nU16TNCS7~isPTa9gw6nF8c3lQpJGFHA2KwTToMJuNk]]></MessageID>\n" + 
			"<Subject><![CDATA[Message title]]></Subject>\n" +
			"<Date>2009-05-03</Date>\n" +
			"<Time>16:15:14</Time>\n" +
			"<Boards>\n" +
			"<Board><![CDATA[en.board1]]></Board>\n" +
			"<Board><![CDATA[en.board2]]></Board>\n" +
			"</Boards>\n" +
			"<ReplyBoard><![CDATA[en.board1]]></ReplyBoard>\n" +
			"<InReplyTo>\n" +
			"<Message>\n" +
			"<Order>0</Order>\n" +
			"<MessageID><![CDATA[afe6519b-7fb2-4533-b172-1f966e79d127@nU16TNCS7~isPTa9gw6nF8c3lQpJGFHA2KwTToMJuNk]]></MessageID>\n" +
			"<MessageURI><![CDATA[SSK@nU16TNCS7~isPTa9gw6nF8c3lQpJGFHA2KwTToMJuNk,FjCiOUGSl6ipOE9glNai9WCp1vPM8k181Gjw62HhYSo,AQACAAE/Freetalk-testing%7cMessageList-123#afe6519b-7fb2-4533-b172-1f966e79d127]]></MessageURI>\n" +
			"</Message>\n" +
			"<Thread>\n" +
			"<MessageID><![CDATA[2a3a8e7e-9e53-4978-a8fd-17b2d92d949c@nU16TNCS7~isPTa9gw6nF8c3lQpJGFHA2KwTToMJuNk]]></MessageID>\n" +
			"<MessageURI><![CDATA[SSK@nU16TNCS7~isPTa9gw6nF8c3lQpJGFHA2KwTToMJuNk,FjCiOUGSl6ipOE9glNai9WCp1vPM8k181Gjw62HhYSo,AQACAAE/Freetalk-testing%7cMessageList-123#2a3a8e7e-9e53-4978-a8fd-17b2d92d949c]]></MessageURI>\n" +
			"</Thread>\n" +
			"</InReplyTo>\n" +
			"<Body><![CDATA[Message body\n" +
			"New line]]></Body>\n" +
			"<Attachments>\n" +
			"<File>\n" +
			"<Key><![CDATA[KSK@attachment1]]></Key>\n" +
			"<Size><![CDATA[10001]]></Size>\n" +
			"</File>\n" +
			"<File>\n" +
			"<Key><![CDATA[KSK@attachment2]]></Key>\n" +
			"<Size><![CDATA[10002]]></Size>\n" +
			"</File>\n" +
			"</Attachments>\n" +
			"</Message>\n" +
			"</Freetalk-testing>\n" 
			);
	}

	public void testEncode() throws Exception {
		System.gc(); db.purge(); System.gc();
		
		ByteArrayOutputStream encodedMessage = new ByteArrayOutputStream(4096);
		WoTMessageXML.encode(mMessageManager.get(mMessageID), encodedMessage);
		
		assertEquals(mHardcodedEncodedMessage, encodedMessage.toString().replace("\r\n", "\n"));
	}

	public void testDecode() throws Exception {
		System.gc(); db.purge(); System.gc();
		
		ByteArrayInputStream is = new ByteArrayInputStream(mHardcodedEncodedMessage.getBytes("UTF-8"));
		ByteArrayOutputStream decodedAndEncodedMessage = new ByteArrayOutputStream(4096);
		Message decodedMessage = WoTMessageXML.decode(mMessageManager, is, (WoTMessageList)mMessageManager.getMessageList(mMessageListID), mMessageRealURI);
		decodedMessage.initializeTransient(mFreetalk);
		WoTMessageXML.encode(decodedMessage, decodedAndEncodedMessage);		
		
		assertEquals(mHardcodedEncodedMessage, decodedAndEncodedMessage.toString().replace("\r\n", "\n"));
	}
}
