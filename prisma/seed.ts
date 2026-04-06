import { PrismaClient } from '@prisma/client';

const prisma = new PrismaClient();

async function main() {
  await prisma.user.createMany({
    data: [
      {
        name: 'Admin',
        email: 'admin@bms.com',
        role: 'admin',
        isActive: true,
      },
      {
        name: 'User 1',
        email: 'user1@bms.com',
        role: 'user',
        isActive: true,
      },
    ],
  });

  console.log('🌱 Seed data created');
}

main()
  .catch((e) => {
    console.error(e);
    process.exit(1);
  })
  .finally(async () => {
    await prisma.$disconnect();
  });