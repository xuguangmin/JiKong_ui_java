package device.protocol;

/**
 * @author remote
 *
 */
public interface IProtocol 
{
	/*
	 *  type ��ͨ��TCP����ͨ��UDP���� DATA_TYPE_TCP  DATA_TYPE_UDP
	 * 
	 */
	public boolean SendPacket(byte buffer[], int len, int type);
	public void ProtocolPdu(PDU pdu);
}
