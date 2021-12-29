import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Advent of Code 2021/12/16 Part 2
 */
public class AOC202116Part2 {

	public static void main(String[] args) throws IOException {

		// check code
		runTests();

		// read input
		BufferedReader br = new BufferedReader(new InputStreamReader(AOC202111.class.getResourceAsStream("AOC202116_input.txt")));
		String line = br.readLine();
		br.close();

		// decode input
		Decoder d = new AOC202116Part2().new Decoder();
		d.decode(line);

		System.out.println("input version sum is: " + d.versionSum);
	}


	public static void runTests() {
		Decoder d = new AOC202116Part2().new Decoder();
		d.decode("8A004A801A8002F478");
		if (d.versionSum != 16) {
			throw new RuntimeException("example 1 invalid");
		}

		d = new AOC202116Part2().new Decoder();
		d.decode("620080001611562C8802118E34");
		if (d.versionSum != 12) {
			throw new RuntimeException("example 2 invalid");
		}

		d = new AOC202116Part2().new Decoder();
		d.decode("C0015000016115A2E0802F182340");
		if (d.versionSum != 23) {
			throw new RuntimeException("example 3 invalid");
		}

		d = new AOC202116Part2().new Decoder();
		d.decode("A0016C880162017C3686B18A3D4780");
		if (d.versionSum != 31) {
			throw new RuntimeException("example 4 invalid");
		}

		// operations tests
		d = new AOC202116Part2().new Decoder();
		List<Long> v = d.decode("C200B40A82");
		if (v.get(0).longValue() != 3) {
			throw new RuntimeException("example 2.1 invalid");
		}

	}

	public class Decoder {

		int versionSum = 0;

		List<Long> decode(String hex) {
			BitStore bs = new BitStore(hex, true);
			List<Long> r = decode(bs, null, false);
			
			System.out.println("version sum is: " + versionSum + ", decoded values: " + String.join(",", r.stream().map(v -> String.valueOf(r)).collect(Collectors.toList())));
			
			return r;
		}


		/**
		 * Decode bit stream
		 * 
		 * @param bs Bit store
		 * @param maxPackets Max packets to decode, null = decode all
		 * @param subPacket is next packet a sub packet?
		 */
		List<Long> decode(BitStore bs, Integer maxPackets, boolean subPacket) {
			List<Long> values = new ArrayList<>();
			int packetsDecoded = 0;
			while (bs.hasMore()) {
				Integer pv = Integer.parseInt(bs.getNextBits(3), 2);
				Integer pt = Integer.parseInt(bs.getNextBits(3), 2);
				System.out.println("packet version: " + pv + ", packet type: " + pt);

				versionSum += pv;

				// 4 is literal packet
				if (pt == 4) {
					values.add(decodeLiteral(bs, subPacket));
				}
				// any other type is a operator packet
				else {
					values.addAll(decodeOperator(bs, subPacket, pt));
				}

				// break if packet limit is set
				packetsDecoded++;
				if (maxPackets != null && packetsDecoded >= maxPackets) {
					break;
				}
			}

			return values;
		}


		/**
		 * Decode literal value from bit stream
		 * 
		 * @param bs
		 * @param subPacket
		 */
		long decodeLiteral(BitStore bs, boolean subPacket) {
			String c = "";
			while (bs.hasMore()) {
				String g0 = bs.getNextBits(1);
				String g14 = bs.getNextBits(4);
				c += g14;
				if (g0.equals("0")) {
					break;
				}
			}

			long v = Long.parseLong(c, 2);
			System.out.println("literal value: " + v + ", binary value: " + c);

			// embedded packets seem no to contain filler zeros
			if (!subPacket) {
				bs.skipEnd();
			}

			return v;
		}


		/**
		 * Decode an operator packet
		 * 
		 * @param bs
		 * @param subPacket
		 * @param pt
		 */
		List<Long> decodeOperator(BitStore bs, boolean subPacket, Integer pt) {
			List<Long> values = new ArrayList<>();

			char lt = bs.getNextBit();

			// length type 0 means next 15 bits contain the number of bits for sub packets
			if (lt == '0') {
				Integer bc = Integer.parseInt(bs.getNextBits(15), 2);
				String subBits = bs.getNextBits(bc);
				BitStore sbs = new BitStore(subBits, false);

				int tpl = 6 + 1 + 15 + bc;

				System.out.println("operator packet, length type: " + lt + ", sub packet bit count: " + bc + ", sub bits: " + subBits + ", total packet length: " + tpl);
				values.addAll(decode(sbs, null, true));
			}
			// length type 1 means next 11 bits contain the number of sub packets in this packet
			else {
				Integer pc = Integer.parseInt(bs.getNextBits(11), 2);
				System.out.println("operator packet, length type: " + lt + ", sub packet count: " + pc);
				values.addAll(decode(bs, pc, true));
			}

			// apply operations

			// sum
			Long newValue = null;
			if (pt == 0) {
				newValue = values.stream().collect(Collectors.summarizingLong(e -> e.longValue())).getSum();
			}
			// product
			else if (pt == 1) {
				newValue = values.get(0);
				for (int i = 1; i < values.size(); i++) {
					newValue *= values.get(i);
				}
			}
			// min value
			else if (pt == 2) {
				newValue = values.stream().collect(Collectors.summarizingLong(e -> e.longValue())).getMin();
			}
			// max value
			else if (pt == 3) {
				newValue = values.stream().collect(Collectors.summarizingLong(e -> e.longValue())).getMax();
			}
			// greater than
			else if (pt == 5) {
				newValue = values.get(0) > values.get(1) ? 1l : 0l;
			}
			// less than
			else if (pt == 6) {
				newValue = values.get(0) < values.get(1) ? 1l : 0l;
			}
			// equal to
			else if (pt == 7) {
				newValue = values.get(0).longValue() == values.get(1).longValue() ? 1l : 0l;
			}

			if (newValue != null) {
				values.clear();
				values.add(newValue);
			}

			// embedded packets seem no to contain filler zeros
			if (!subPacket) {
				bs.skipEnd();
			}

			return values;
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
