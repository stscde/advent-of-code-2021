import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


/**
 * Advent of Code 2021/12/16 Part 1
 */
public class AOC202116 {

	public static void main(String[] args) throws IOException {

		// check code
		runTests();

		// read input
		BufferedReader br = new BufferedReader(new InputStreamReader(AOC202111.class.getResourceAsStream("AOC202116_input.txt")));
		String line = br.readLine();
		br.close();

		// decode input
		Decoder d = new AOC202116().new Decoder();
		d.decode(line);

		System.out.println("input version sum is: " + d.versionSum);
	}


	public static void runTests() {
		Decoder d = new AOC202116().new Decoder();
		d.decode("8A004A801A8002F478");
		if (d.versionSum != 16) {
			throw new RuntimeException("example 1 invalid");
		}

		d = new AOC202116().new Decoder();
		d.decode("620080001611562C8802118E34");
		if (d.versionSum != 12) {
			throw new RuntimeException("example 2 invalid");
		}

		d = new AOC202116().new Decoder();
		d.decode("C0015000016115A2E0802F182340");
		if (d.versionSum != 23) {
			throw new RuntimeException("example 3 invalid");
		}

		d = new AOC202116().new Decoder();
		d.decode("A0016C880162017C3686B18A3D4780");
		if (d.versionSum != 31) {
			throw new RuntimeException("example 4 invalid");
		}

	}

	public class Decoder {

		int versionSum = 0;

		void decode(String hex) {
			BitStore bs = new BitStore(hex, true);
			decode(bs, null, false);

			System.out.println("version sum is: " + versionSum);
		}


		/**
		 * Decode bit stream
		 * 
		 * @param bs Bit store
		 * @param maxPackets Max packets to decode, null = decode all
		 * @param subPacket is next packet a sub packet?
		 */
		void decode(BitStore bs, Integer maxPackets, boolean subPacket) {
			int packetsDecoded = 0;
			while (bs.hasMore()) {
				Integer pv = Integer.parseInt(bs.getNextBits(3), 2);
				Integer pt = Integer.parseInt(bs.getNextBits(3), 2);
				System.out.println("packet version: " + pv + ", packet type: " + pt);

				versionSum += pv;

				// 4 is literal packet
				if (pt == 4) {
					decodeLiteral(bs, subPacket);
				}
				// any other type is a operator packet
				else {
					decodeOperator(bs, subPacket);
				}

				// break if packet limit is set
				packetsDecoded++;
				if (maxPackets != null && packetsDecoded >= maxPackets) {
					break;
				}
			}
		}


		/**
		 * Decode literal value from bit stream
		 * 
		 * @param bs
		 * @param subPacket
		 */
		void decodeLiteral(BitStore bs, boolean subPacket) {
			String c = "";
			while (bs.hasMore()) {
				String g0 = bs.getNextBits(1);
				String g14 = bs.getNextBits(4);
				c += g14;
				if (g0.equals("0")) {
					break;
				}
			}
			System.out.println("literal value: " + Long.parseLong(c, 2) + ", binary value: " + c);

			// embedded packets seem no to contain filler zeros
			if (!subPacket) {
				bs.skipEnd();
			}
		}


		/**
		 * Decode an operator packet
		 * 
		 * @param bs
		 * @param subPacket
		 */
		void decodeOperator(BitStore bs, boolean subPacket) {
			char lt = bs.getNextBit();

			// length type 0 means next 15 bits contain the number of bits for sub packets
			if (lt == '0') {
				Integer bc = Integer.parseInt(bs.getNextBits(15), 2);
				String subBits = bs.getNextBits(bc);
				BitStore sbs = new BitStore(subBits, false);

				int tpl = 6 + 1 + 15 + bc;

				System.out.println("operator packet, length type: " + lt + ", sub packet bit count: " + bc + ", sub bits: " + subBits + ", total packet length: " + tpl);
				decode(sbs, null, true);
			}
			// length type 1 means next 11 bits contain the number of sub packets in this packet
			else {
				Integer pc = Integer.parseInt(bs.getNextBits(11), 2);
				System.out.println("operator packet, length type: " + lt + ", sub packet count: " + pc);
				decode(bs, pc, true);
			}

			// embedded packets seem no to contain filler zeros
			if (!subPacket) {
				bs.skipEnd();
			}
		}
	}

	public class BitStore {

		List<String> values = new ArrayList<>();

		// value index
		int vi = 0;

		// bit index
		int bi = 0;

		// values can have trailing zeros to fill 4-bit values?
		boolean hasTrailingZeros = true;

		// max bits to deliver
		int deliverLimit = -1;

		// already delivered bits
		int deliveredBits = 0;

		/**
		 * Constructor
		 * 
		 * @param input Input string
		 * @param decodeHex decode input string as hex chars?
		 */
		BitStore(String input, boolean decodeHex) {
			if (decodeHex) {
				for (int i = 0; i < input.length(); i++) {
					String b = "000" + Integer.toString(Integer.parseInt(String.valueOf(input.charAt(i)), 16), 2);
					b = b.substring(b.length() - 4, b.length());
					values.add(b);
				}
			} else {
				values.add(input);
			}
		}


		/**
		 * Get next bit
		 * 
		 * @return
		 */
		char getNextBit() {
			// System.out.println("vi: " + vi + ", bi: " + bi + ", values: " + values.size() + "=" + String.join(",", values));
			if (bi > values.get(vi).length() - 1) {
				vi++;
				bi = 0;
			}
			char c = values.get(vi).charAt(bi);
			bi++;
			return c;
		}


		/**
		 * Get n next bits
		 * 
		 * @param count
		 * @return
		 */
		String getNextBits(int count) {
			String s = "";
			for (int i = 0; i < count; i++) {
				s += getNextBit();
			}
			return s;
		}


		/**
		 * Skip remaining bits of current value but not if none of the current values bits has been consumed
		 */
		void skipEnd() {
			if (bi > 0) {
				vi++;
				bi = 0;
			}

			while (vi < values.size() && values.get(vi).equals("0000")) {
				vi++;
			}
		}


		/**
		 * Are more bits available?
		 * 
		 * @return
		 */
		boolean hasMore() {
			// System.out.println("vi: " + vi + ", bi: " + bi + ", values: " + values.size() + "=" + String.join(",", values));

			// when on last value check bit index
			if (vi >= values.size() - 1) {
				return vi < values.size() && bi < values.get(vi).length() - 1;
			}

			return true;
		}

	}
}
